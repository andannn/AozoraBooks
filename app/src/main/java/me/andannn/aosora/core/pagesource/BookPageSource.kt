package me.andannn.aosora.core.pagesource

import kotlinx.coroutines.flow.Flow

/**
 * Book page source.
 */
interface BookPageSource<T> {
    val pagerSnapShotFlow: Flow<PagerSnapShot<T>>
}

data class PagerSnapShot<T>(
    val initialIndex: Int?,
    val pageList: List<T>,
    val snapshotVersion: Int,
)

