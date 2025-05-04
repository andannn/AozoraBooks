package me.andannn.aozora.core.pagesource

import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock.System
import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraCoverPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraLayoutPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.data.common.BookMeta
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.page.builder.PageBuilder
import me.andannn.aozora.core.pagesource.page.builder.createPageBuilder
import me.andannn.aozora.core.pagesource.page.createPageFlowFromSequence
import me.andannn.aozora.core.pagesource.raw.BookRawSource

private const val TAG = "CachedLinerPageSource"

/**
 * the parsed result is cached in this class.
 * use catch when [getPagerSnapShotFlow] is called if available,
 */
abstract class CachedLinerPageSource(
    private val rawSource: BookRawSource,
    private val useRoughPageBuilder: Boolean = false,
) : BookPageSource {
    private val cachedBlockList = mutableListOf<AozoraBlock>()
    private var isCachedCompleted: Boolean = false
    private var version: Int = 0

    private var bookMetaData: BookMeta? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPagerSnapShotFlow(
        pageMetaData: PageMetaData,
        initialProgress: Long,
    ): Flow<PagerSnapShot> =
        flow {
            Napier.d(tag = TAG) { "Get page snapshot flow initialProgress $initialProgress, metaData $pageMetaData. " }

            version++

            bookMetaData = rawSource.getBookMeta()
            val rawSourceFlow = rawSource.getRawSource()

            val loadedPages = mutableListOf<AozoraPage>()
            var initialPageIndex: Int? = null

            var startMilliseconds = System.now().toEpochMilliseconds()
            Napier.d(tag = TAG) { "start load page. " }

            createPageFlow(pageMetaData, rawSourceFlow)
                .onEach { Napier.v(tag = TAG) { "page added ${it::class.simpleName} ${(it as? AozoraRoughPage)?.progressRange}" } }
                .chunked(CHUNK_SIZE)
                .collectIndexed { index, pageList ->
                    loadedPages.addAll(pageList)
                    if (initialProgress == 0L && index == 0) {
                        Napier.d(tag = TAG) { "hit first page" }
                        initialPageIndex = 0
                    }

                    val hitIndex =
                        pageList.indexOfFirst { initialProgress in ((it as? AozoraRoughPage)?.progressRange ?: -1L..0L) }
                    if (initialPageIndex == null && hitIndex != -1) {
                        Napier.d(tag = TAG) { "hit initial. index: $hitIndex}" }
                        initialPageIndex = hitIndex + index * CHUNK_SIZE
                    }

                    if (initialPageIndex != null) {
                        emit(
                            PagerSnapShot(
                                initialPageIndex,
                                loadedPages.toImmutableList(),
                                version,
                            ),
                        )
                    }
                }

            Napier.d(tag = TAG) {
                "end load page. pages: ${loadedPages.size} in ${
                    System.now().toEpochMilliseconds() - startMilliseconds
                }"
            }
        }

    private fun createPageFlow(
        pageMetaData: PageMetaData,
        rawSourceFlow: Flow<AozoraBlock>,
    ): Flow<AozoraPage> {
        val pageFlow =
            if (useRoughPageBuilder) {
                createPageFlowFromSequence<AozoraRoughPage>(
                    blockSequenceFlow = rawSourceFlow.cachedOrSource(),
                    builderFactory = {
                        createPageBuilder(
                            pageMetaData,
                            useRoughPageBuilder,
                        ) as PageBuilder<AozoraRoughPage>
                    },
                )
            } else {
                createPageFlowFromSequence<AozoraLayoutPage>(
                    blockSequenceFlow = rawSourceFlow.cachedOrSource(),
                    builderFactory = {
                        createPageBuilder(
                            pageMetaData,
                            useRoughPageBuilder,
                        ) as PageBuilder<AozoraLayoutPage>
                    },
                )
            }

        return pageFlow
            .onStart {
                emit(
                    AozoraCoverPage(
                        pageMetaData = pageMetaData,
                        title = bookMetaData!!.title,
                        author = bookMetaData!!.author,
                        subtitle = bookMetaData!!.subtitle,
                    ),
                )
            }.flowOn(Dispatchers.IO)
    }

    override fun dispose() {
        Napier.d(tag = TAG) { "dispose called" }
        rawSource.dispose()
    }

    /**
     * return cached block list if available. or return source flow.
     */
    private fun Flow<AozoraBlock>.cachedOrSource(): Flow<AozoraBlock> =
        channelFlow {
            if (isCachedCompleted) {
                Napier.d(tag = TAG) { "Using cached source." }
                cachedBlockList.forEach { block ->
                    send(block)
                }
                return@channelFlow
            }

            cachedBlockList.clear()

            Napier.d(tag = TAG) { "Using raw source" }
            this@cachedOrSource.collect { block ->
                cachedBlockList.add(block)
                send(block)
            }

            isCachedCompleted = true
        }

    companion object {
        private const val CHUNK_SIZE = 20
    }
}
