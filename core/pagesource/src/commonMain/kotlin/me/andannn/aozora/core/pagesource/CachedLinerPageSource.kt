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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock.System
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraCoverPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.data.common.Block
import me.andannn.aozora.core.data.common.BookMeta
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.measure.DefaultMeasurer
import me.andannn.aozora.core.pagesource.page.builder.RoughPageBuilder
import me.andannn.aozora.core.pagesource.page.createPageFlowFromSequence
import me.andannn.aozora.core.pagesource.raw.BookRawSource

private const val TAG = "CachedLinerPageSource"

/**
 * the parsed result is cached in this class.
 * use catch when [getPagerSnapShotFlow] is called if available,
 */
abstract class CachedLinerPageSource(
    private val rawSource: BookRawSource,
) : BookPageSource {
    private val cachedBlockList = mutableListOf<Block>()
    private var isCachedCompleted: Boolean = false
    private var version: Int = 0

    private var bookMetaData: BookMeta? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPagerSnapShotFlow(
        pageMetaData: PageMetaData,
        initialBlockIndex: Int?,
    ): Flow<PagerSnapShot> =
        flow {
            Napier.d(tag = TAG) { "Get page snapshot flow initialProgress $initialBlockIndex, metaData $pageMetaData. " }

            version++
            bookMetaData = rawSource.getBookMeta()

            val loadedPages = mutableListOf<AozoraPage>()
            var initialPageIndex: Int? = null
            var startMilliseconds = System.now().toEpochMilliseconds()
            Napier.d(tag = TAG) { "start load page. " }

            createPageFlow(
                pageMetaData = pageMetaData,
                rawSourceProvider = { rawSource.getRawSource() },
            ).chunked(CHUNK_SIZE)
                .collectIndexed { index, pageList ->
                    loadedPages.addAll(pageList)

                    if (initialPageIndex == null) {
                        if (initialBlockIndex == null) {
                            Napier.d(tag = TAG) { "hit Book cover page" }
                            initialPageIndex = 0
                        } else {
                            val hitIndex =
                                pageList.indexOfFirst {
                                    initialBlockIndex in (
                                        (it as? AozoraRoughPage)?.pageProgress
                                            ?: -1L..0L
                                    )
                                }
                            if (hitIndex != -1) {
                                Napier.d(tag = TAG) { "hit initial. index: $hitIndex}" }
                                initialPageIndex = hitIndex + index * CHUNK_SIZE
                            }
                        }
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
        rawSourceProvider: suspend () -> Flow<Block>,
    ): Flow<AozoraPage> {
        val pageFlow: Flow<AozoraPage> =
            createPageFlowFromSequence<AozoraRoughPage>(
                blockSequenceFlow = cachedOrSource(rawSourceProvider),
                builderFactory = {
                    RoughPageBuilder(meta = pageMetaData, measurer = DefaultMeasurer(pageMetaData))
                },
            )

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
            }.onCompletion {
                emit(
                    AozoraPage.AozoraBibliographicalPage(
                        pageMetaData = pageMetaData,
                        html = rawSource.getBookMeta().bibliographicalInformation,
                    ),
                )
            }.flowOn(Dispatchers.IO)
    }

    /**
     * return cached block list if available. or return source flow.
     */
    private fun cachedOrSource(rawSourceProvider: suspend () -> Flow<Block>): Flow<Block> =
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
            rawSourceProvider().collect { block ->
                cachedBlockList.add(block)
                send(block)
            }

            isCachedCompleted = true
        }

    companion object {
        private const val CHUNK_SIZE = 20
    }
}
