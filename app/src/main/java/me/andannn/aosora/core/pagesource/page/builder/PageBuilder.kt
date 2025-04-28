package me.andannn.aosora.core.pagesource.page.builder

import me.andannn.aosora.core.common.model.AozoraBlock
import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.common.model.AozoraPage

private const val TAG = "ReaderPageBuilder"

sealed interface FillResult {
    data class Filled(
        val remainElement: AozoraElement? = null,
        val remainBlock: AozoraBlock? = null,
    ) : FillResult

    data object FillContinue : FillResult
}


interface PageBuilder<out T : AozoraPage> {
    fun tryAddBlock(block: AozoraBlock): FillResult

    fun build(): T
}


