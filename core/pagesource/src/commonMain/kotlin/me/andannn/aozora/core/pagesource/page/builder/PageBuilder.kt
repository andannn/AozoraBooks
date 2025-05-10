/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page.builder

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.Block

sealed interface FillResult {
    data class Filled(
        val remainElement: AozoraElement? = null,
        val remainBlock: Block? = null,
    ) : FillResult

    data object FillContinue : FillResult
}

interface PageBuilder<out T : AozoraPage> {
    fun tryAddBlock(block: Block): FillResult

    fun build(): T
}
