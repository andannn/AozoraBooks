package me.andannn.aosora.core.pagesource

import kotlinx.coroutines.flow.Flow
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData

/**
 * Book page source.
 */
interface BookPageSource<out T: AozoraPage> {
    /**
     * generated pager snap shot flow by [metaData].
     *
     * @param metaData page meta data.
     * @param initialProgress initial start progress of book page source. every 64 bytes is One Unit of progress.
     */
    fun getPagerSnapShotFlow(metaData: PageMetaData, initialProgress: Long = 0): Flow<PagerSnapShot<T>>

    fun dispose()
}

/**
 * Pager snap shot.
 *
 * @param initialIndex initial page index.
 * @param pageList page list.
 * @param snapshotVersion snapshot version.
 */
data class PagerSnapShot<out T: AozoraPage>(
    val initialIndex: Int?,
    val pageList: List<T>,
    val snapshotVersion: Int,
)

