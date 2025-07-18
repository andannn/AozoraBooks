/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.pagesource

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AozoraPage
import me.andannn.aozora.core.domain.model.PageMetaData
import me.andannn.aozora.core.domain.model.ReadProgress
import me.andannn.aozora.core.domain.model.TableOfContentsModel

val LocalBookPageSource: ProvidableCompositionLocal<BookPageSource> =
    compositionLocalOf { error("no book source") }

class SourceNotFoundException(
    override val cause: Throwable?,
) : Exception("Book source not found", cause)

/**
 * Book page source.
 */
interface BookPageSource {
    /**
     * generated pager snap shot flow by [pageMetaData].
     *
     * @param pageMetaData page meta data.
     * @param readingProgress initial start progress of book page source. every 64 bytes is One Unit of progress.
     * @throws SourceNotFoundException when error occurred.
     * @throws CopyRightRetainedException when try to download source with copyright.
     */
    fun getPagerSnapShotFlow(
        pageMetaData: PageMetaData,
        readingProgress: ReadProgress,
    ): Flow<PagerSnapShot>

    /**
     * get book meta. return empty list when error occurred.
     * @throws SourceNotFoundException when error occurred.
     * @throws CopyRightRetainedException when try to download source with copyright.
     */
    suspend fun getTableOfContents(): List<TableOfContentsModel>

    /**
     * get total block count. return null when error occurred.
     * @throws SourceNotFoundException when error occurred.
     * @throws CopyRightRetainedException when try to download source with copyright.
     */
    suspend fun getTotalBlockCount(): Int?

    /**
     * close this source.
     */
    fun close()

    interface Factory {
        fun createDummySource(): BookPageSource

        fun createBookPageSource(
            card: AozoraBookCard,
            scope: CoroutineScope,
        ): BookPageSource
    }
}

sealed interface PagerSnapShot {
    /**
     * Pager snap shot.
     */
    data class Ready(
        val initialIndex: Int?,
        val pageList: ImmutableList<AozoraPage>,
        val snapshotVersion: Int,
    ) : PagerSnapShot

    data class Error(
        val exception: Throwable,
    ) : PagerSnapShot
}
