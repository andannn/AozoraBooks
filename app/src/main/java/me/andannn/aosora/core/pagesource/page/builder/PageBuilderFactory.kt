package me.andannn.aosora.core.pagesource.page.builder

import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pagesource.measure.DefaultMeasurer

fun <T : AozoraPage> createPageBuilder(meta: PageMetaData): PageBuilder<T> {
    return PAGE_BUILDER_FACTORY_LIST.firstNotNullOf { it.create(meta) as? PageBuilder<T> }
}

private interface PageBuilderFactory {
    fun create(meta: PageMetaData): PageBuilder<AozoraPage>
}

private val PAGE_BUILDER_FACTORY_LIST = listOf(
    RoughPageBuilderFactory,
    LayoutPageBuilderFactory,
)

private object RoughPageBuilderFactory : PageBuilderFactory {
    override fun create(meta: PageMetaData): PageBuilder<AozoraPage> {
        return RoughPageBuilder(
            meta = meta,
            measurer = DefaultMeasurer(meta)
        )
    }
}

private object LayoutPageBuilderFactory : PageBuilderFactory {
    override fun create(meta: PageMetaData): PageBuilder<AozoraPage> {
        return LayoutPageBuilder(
            meta = meta,
            measurer = DefaultMeasurer(meta)
        )
    }
}
