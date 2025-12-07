/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource

import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformWhile
import me.andannn.aozora.core.domain.model.Page
import me.andannn.aozora.core.domain.model.Page.CoverPage
import me.andannn.aozora.core.domain.model.PageMetaData
import me.andannn.aozora.core.domain.model.READ_PROGRESS_DONE
import me.andannn.aozora.core.domain.model.READ_PROGRESS_NONE
import me.andannn.aozora.core.domain.model.ReadProgress
import me.andannn.aozora.core.domain.model.TableOfContentsModel
import me.andannn.aozora.core.domain.pagesource.BookPageSource
import me.andannn.aozora.core.domain.pagesource.PagerSnapShot
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.page.LayoutPageBuilder
import me.andannn.aozora.core.pagesource.page.createPageFlowFromSequence
import me.andannn.aozora.core.pagesource.raw.BookInfo
import me.andannn.aozora.core.pagesource.raw.BookRawSource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TAG = "CachedLinerPageSource"

/**
 * the parsed result is cached in this class.
 * use catch when [getPagerSnapShotFlow] is called if available,
 */
internal class CachedLinerPageSource(
    private val rawSource: BookRawSource,
) : BookPageSource {
    private var version: Int = 0
    private val job = Job()
    private val shardBlockHotFlow: SharedFlow<ParseEvent> =
        rawSource
            .getRawSource()
            .map {
                ParseEvent.Block(it) as ParseEvent
            }.onCompletion {
                emit(ParseEvent.Completed)
            }.catch {
                emit(ParseEvent.Error(it))
            }.shareIn(
                CoroutineScope(Dispatchers.IO + job),
                replay = Int.MAX_VALUE,
                started = SharingStarted.Eagerly,
            )

    @OptIn(ExperimentalTime::class)
    override fun getPagerSnapShotFlow(
        pageMetaData: PageMetaData,
        readingProgress: ReadProgress,
    ): Flow<PagerSnapShot> =
        flow<PagerSnapShot> {
            Napier.d(tag = TAG) { "Get page snapshot flow initialProgress $readingProgress, metaData $pageMetaData. " }

            val version = version++

            val bookInfoData = rawSource.getBookInfo()
            val loadedPages = mutableListOf<Page>()
            var initialPageIndex: Int? = null
            val startMilliseconds = Clock.System.now().toEpochMilliseconds()
            Napier.d(tag = TAG) { "start load page. " }

            createPageFlow(
                bookInfoData = bookInfoData,
                pageMetaData = pageMetaData,
            ).collectIndexed { index, page ->
                loadedPages.add(page)

                if (initialPageIndex == null) {
                    val hit =
                        when (page) {
                            is CoverPage -> {
                                readingProgress is ReadProgress.None
                            }

                            is Page.BibliographicalPage -> {
                                readingProgress is ReadProgress.Done
                            }

                            is Page.LayoutPage -> {
                                readingProgress is ReadProgress.Reading && readingProgress.blockIndex in page.pageProgress
                            }
                        }
                    if (hit) {
                        Napier.d(tag = TAG) { "hit initial. index: $index}" }
                        initialPageIndex = index
                    }
                }

                if (initialPageIndex != null) {
                    emit(
                        PagerSnapShot.Ready(
                            snapshotVersion = version,
                            pageList = loadedPages.toImmutableList(),
                            initialIndex = initialPageIndex,
                        ),
                    )
                }
            }

            Napier.d(tag = TAG) {
                "end load page. pages: ${loadedPages.size} in ${
                    Clock.System.now().toEpochMilliseconds() - startMilliseconds
                }"
            }
        }.catch {
            emit(
                PagerSnapShot.Error(it),
            )
        }

    private fun createPageFlow(
        bookInfoData: BookInfo,
        pageMetaData: PageMetaData,
    ): Flow<Page> {
        val coldFlow =
            shardBlockHotFlow.transformWhile { state ->
                when (state) {
                    is ParseEvent.Block -> {
                        emit(state.block)
                        true
                    }

                    ParseEvent.Completed -> false
                    is ParseEvent.Error -> throw state.t
                }
            }
        val pageFlow: Flow<Page> =
            createPageFlowFromSequence(
                blockSequenceFlow = coldFlow,
                builderFactory = {
                    LayoutPageBuilder(
                        meta = pageMetaData,
                    )
                },
            )

        return pageFlow
            .onStart {
                emit(
                    CoverPage(
                        pageMetaData = pageMetaData,
                        title = bookInfoData.title,
                        author = bookInfoData.author,
                        subtitle = bookInfoData.subtitle,
                    ),
                )
            }.onCompletion {
                emit(
                    Page.BibliographicalPage(
                        pageMetaData = pageMetaData,
                        html = rawSource.getBookInfo().bibliographicalInformation,
                    ),
                )
            }
    }

    override suspend fun getTableOfContents(): List<TableOfContentsModel> {
        val bookInfo =
            try {
                rawSource.getBookInfo()
            } catch (e: Exception) {
                Napier.e(tag = TAG) { "Get table of contents failed. $e" }
                return emptyList()
            }

        val maxHeadingLevel = bookInfo.tableOfContentList.minOfOrNull { it.headingLevel } ?: 3
        return sequence {
            // 表紙
            yield(
                TableOfContentsModel(
                    headingLevel = maxHeadingLevel,
                    title = "表紙",
                    blockIndex = READ_PROGRESS_NONE,
                ),
            )
            yieldAll(
                bookInfo.tableOfContentList
                    .map {
                        TableOfContentsModel(
                            headingLevel = it.headingLevel,
                            title = it.title,
                            blockIndex = it.lineNumber,
                        )
                    },
            )
            yield(
                TableOfContentsModel(
                    headingLevel = maxHeadingLevel,
                    title = "奥付",
                    blockIndex = READ_PROGRESS_DONE,
                ),
            )
        }.toList()
    }

    override suspend fun getTotalBlockCount(): Int? =
        try {
            rawSource.getBookInfo().blockCount
        } catch (e: Exception) {
            null
        }

    override fun close() {
        job.cancel()
    }
}

private sealed interface ParseEvent {
    data class Block(
        val block: AozoraBlock,
    ) : ParseEvent

    data object Completed : ParseEvent

    data class Error(
        val t: Throwable,
    ) : ParseEvent
}
