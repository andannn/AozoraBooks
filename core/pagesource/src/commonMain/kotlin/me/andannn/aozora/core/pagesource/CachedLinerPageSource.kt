package me.andannn.aozora.core.pagesource

import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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
import me.andannn.aozora.core.data.common.BookInfo
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPagerSnapShotFlow(
        pageMetaData: PageMetaData,
        initialBlockIndex: Int?,
    ): Flow<PagerSnapShot> =
        flow {
            Napier.d(tag = TAG) { "Get page snapshot flow initialProgress $initialBlockIndex, metaData $pageMetaData. " }

            val version = version++

            val bookInfoData = rawSource.getBookInfo()
            bookInfoData.tableOfContentList
            val loadedPages = mutableListOf<AozoraPage>()
            var initialPageIndex: Int? = null
            var startMilliseconds = System.now().toEpochMilliseconds()
            Napier.d(tag = TAG) { "start load page. " }

            createPageFlow(
                bookInfoData = bookInfoData,
                pageMetaData = pageMetaData,
                rawSourceProvider = { rawSource.getRawSource() },
            ).collectIndexed { index, page ->
                loadedPages.add(page)

                if (initialPageIndex == null) {
                    if (initialBlockIndex == null) {
                        Napier.d(tag = TAG) { "hit Book cover page" }
                        initialPageIndex = 0
                    } else {
                        var hit = false
                        when (page) {
                            is AozoraCoverPage -> {
                                hit = initialBlockIndex == -1
                            }

                            is AozoraRoughPage -> {
                                hit = initialBlockIndex in page.pageProgress
                            }

                            is AozoraPage.AozoraBibliographicalPage -> {
                                hit = initialBlockIndex == Int.MAX_VALUE
                            }
                        }
                        if (hit) {
                            Napier.d(tag = TAG) { "hit initial. index: $index}" }
                            initialPageIndex = index
                        }
                    }
                }

                if (initialPageIndex != null) {
                    emit(
                        PagerSnapShot(
                            snapshotVersion = version,
                            pageList = loadedPages.toImmutableList(),
                            initialIndex = initialPageIndex,
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
        bookInfoData: BookInfo,
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
                        title = bookInfoData.title,
                        author = bookInfoData.author,
                        subtitle = bookInfoData.subtitle,
                    ),
                )
            }.onCompletion {
                emit(
                    AozoraPage.AozoraBibliographicalPage(
                        pageMetaData = pageMetaData,
                        html = rawSource.getBookInfo().bibliographicalInformation,
                    ),
                )
            }.flowOn(Dispatchers.IO)
    }

    override suspend fun getBookInfo(): BookInfo = rawSource.getBookInfo()

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
}
