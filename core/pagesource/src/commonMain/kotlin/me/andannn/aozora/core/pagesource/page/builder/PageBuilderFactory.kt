package me.andannn.aozora.core.pagesource.page.builder

import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraLayoutPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.measure.DefaultMeasurer

fun createPageBuilder(meta: PageMetaData, useRoughPageBuilder: Boolean = true): PageBuilder<AozoraPage> {
    return if (useRoughPageBuilder) {
        RoughPageBuilderFactory.create(meta)
    } else {
        LayoutPageBuilderFactory.create(meta)
    }
}

private interface PageBuilderFactory<T : AozoraPage> {
    fun create(meta: PageMetaData): PageBuilder<AozoraPage>
}

private object RoughPageBuilderFactory : PageBuilderFactory<AozoraRoughPage> {
    override fun create(meta: PageMetaData): PageBuilder<AozoraRoughPage> {
        return RoughPageBuilder(
            meta = meta,
            measurer = DefaultMeasurer(meta)
        )
    }
}

private object LayoutPageBuilderFactory : PageBuilderFactory<AozoraLayoutPage> {
    override fun create(meta: PageMetaData): PageBuilder<AozoraLayoutPage> {
        return LayoutPageBuilder(
            meta = meta,
            measurer = DefaultMeasurer(meta)
        )
    }
}
