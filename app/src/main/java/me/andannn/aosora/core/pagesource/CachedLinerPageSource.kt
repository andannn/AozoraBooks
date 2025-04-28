package me.andannn.aosora.core.pagesource

import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import me.andannn.aosora.core.common.model.AozoraBlock
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.AozoraPage.AozoraRoughPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pagesource.page.builder.createPageBuilder
import me.andannn.aosora.core.pagesource.page.createPageFlowFromSequence
import me.andannn.aosora.core.pagesource.raw.BookRawSource

private const val TAG = "CachedPageSource"

/**
 * the parsed result is cached in this class.
 * use catch when [getPagerSnapShotFlow] is called if available,
 */
abstract class CachedLinerPageSource<out T : AozoraPage>(
    private val rawSource: BookRawSource
): BookPageSource<T> {
    private val cachedBlockList = mutableListOf<AozoraBlock>()
    private var isCachedCompleted: Boolean = false
    private var version: Int = 0

    override fun getPagerSnapShotFlow(
        metaData: PageMetaData,
        initialProgress: Long
    ): Flow<PagerSnapShot<T>> = flow {
        Napier.d(tag = TAG) { "Get page snapshot flow initialProgress $initialProgress, metaData $metaData. " }

        val pageFlow = createPageFlowFromSequence<T>(
            blockSequenceFlow = rawSource.getRawSource().cachedOrSource(),
            builderFactory = { createPageBuilder<T>(metaData) }
        )

        val loadedPages = mutableListOf<T>()
        var initialPageIndex: Int? = null

        pageFlow
            .onEach {
                Napier.d(tag = TAG) { "page added ${(it as? AozoraRoughPage)?.progressRange}" }
            }
            .flowOn(Dispatchers.IO)
            .collectIndexed { index, page ->
                loadedPages.add(page)
                if (initialPageIndex == null && initialProgress in (page as AozoraRoughPage).progressRange) {
                    Napier.d(tag = TAG) { "hit initial. index: $index, range: ${page.progressRange}" }
                    initialPageIndex = index
                }
                if (initialPageIndex != null) {
                    emit(PagerSnapShot<T>(initialPageIndex, loadedPages, version++))
                }
            }

        Napier.d(tag = TAG) { "all page loaded. $loadedPages" }
    }

    /**
     * return cached block list if available. or return source flow.
     */
    private fun Flow<AozoraBlock>.cachedOrSource(): Flow<AozoraBlock> = channelFlow {
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
}