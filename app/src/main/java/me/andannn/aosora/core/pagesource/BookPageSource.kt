package me.andannn.aosora.core.pagesource

import me.andannn.aosora.core.common.model.AozoraPage
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

