/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.TableOfContentsModel

val LocalBookPageSource: ProvidableCompositionLocal<BookPageSource> =
    compositionLocalOf { error("no book source") }

/**
 * Book page source.
 */
interface BookPageSource {
    /**
     * generated pager snap shot flow by [pageMetaData].
     *
     * @param pageMetaData page meta data.
     * @param initialBlockIndex initial start progress of book page source. every 64 bytes is One Unit of progress.
     */
    fun getPagerSnapShotFlow(
        pageMetaData: PageMetaData,
        initialBlockIndex: Int?,
    ): Flow<PagerSnapShot>

    /**
     * get book meta.
     */
    suspend fun getTableOfContents(): List<TableOfContentsModel>
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
