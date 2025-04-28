package me.andannn.aosora.core.pagesource.page.builder

import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.AozoraPage.AozoraLayoutPage
import me.andannn.aosora.core.common.model.AozoraPage.AozoraRoughPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pagesource.measure.DefaultMeasurer

fun <T : AozoraPage> createPageBuilder(meta: PageMetaData): PageBuilder<T> {
    return PAGE_BUILDER_FACTORY_LIST.firstNotNullOf { it.create(meta) as? PageBuilder<T> }
}

private interface PageBuilderFactory<T : AozoraPage> {
    fun create(meta: PageMetaData): PageBuilder<AozoraPage>
}

private val PAGE_BUILDER_FACTORY_LIST = listOf(
    RoughPageBuilderFactory,
    LayoutPageBuilderFactory,
)

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
