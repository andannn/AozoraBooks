package me.andannn.aosora.core.source

import me.andannn.aosora.core.pager.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData

/**
 * Book page source.
 */
interface BookPageSource {
    /**
     * Create page source.
     */
    fun pageSource(meta: PageMetaData): Sequence<AozoraPage>
}

