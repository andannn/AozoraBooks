package me.andannn.aozora.core.pagesource.page.builder

import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraLayoutPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.measure.DefaultMeasurer

private interface PageBuilderFactory<T : AozoraPage> {
    fun create(meta: PageMetaData): PageBuilder<AozoraPage>
}

object RoughPageBuilderFactory : PageBuilderFactory<AozoraRoughPage> {
    override fun create(meta: PageMetaData): PageBuilder<AozoraRoughPage> =
        RoughPageBuilder(
            meta = meta,
            measurer = DefaultMeasurer(meta),
        )
}

object LayoutPageBuilderFactory : PageBuilderFactory<AozoraLayoutPage> {
    override fun create(meta: PageMetaData): PageBuilder<AozoraLayoutPage> =
        LayoutPageBuilder(
            meta = meta,
            measurer = DefaultMeasurer(meta),
        )
}
