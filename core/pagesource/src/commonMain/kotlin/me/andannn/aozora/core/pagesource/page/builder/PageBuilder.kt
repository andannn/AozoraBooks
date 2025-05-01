package me.andannn.aozora.core.pagesource.page.builder

import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraPage

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
