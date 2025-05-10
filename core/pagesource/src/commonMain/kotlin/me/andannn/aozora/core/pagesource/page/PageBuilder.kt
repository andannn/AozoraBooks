/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraPage

internal sealed interface FillResult {
    data class Filled(
        val remainElement: AozoraElement? = null,
        val remainBlock: AozoraBlock? = null,
    ) : FillResult

    data object FillContinue : FillResult
}

internal interface PageBuilder<out T : AozoraPage> {
    fun tryAddBlock(block: AozoraBlock): FillResult

    fun build(): T
}
