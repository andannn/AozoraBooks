package me.andannn.aosora.core.pagesource.page.builder

import me.andannn.aosora.core.common.model.AozoraBlock
import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.PageContext
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pagesource.measure.DefaultMeasurer

private const val TAG = "ReaderPageBuilder"

sealed interface FillResult {
    data class Filled(
        val remainElement: AozoraElement? = null,
        val remainBlock: AozoraBlock? = null,
    ) : FillResult

    data object FillContinue : FillResult
}

inline fun <reified T : AozoraPage> createReaderPageBuilder(
    meta: PageMetaData,
): PageBuilder<T> {
    val builder = when (T::class) {
        AozoraPage.AozoraRoughPage::class -> RoughPageBuilder(
            meta = meta,
            measurer = DefaultMeasurer(meta)
        )
        else -> LayoutPageBuilder(
            meta = meta,
            measurer = DefaultMeasurer(meta)
        )
    }

    @Suppress("UNCHECKED_CAST")
    return builder as PageBuilder<T>
}

interface PageBuilder<out T : AozoraPage> {
    fun tryAddBlock(block: AozoraBlock): FillResult

    fun build(): T
}


