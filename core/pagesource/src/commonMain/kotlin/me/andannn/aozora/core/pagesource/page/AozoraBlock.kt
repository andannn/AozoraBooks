/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.domain.model.Block
import me.andannn.aozora.core.domain.model.debugText

internal sealed class AozoraBlock(
    override val blockIndex: Int,
    open val elements: List<AozoraElement>,
) : Block {
    data class TextBlock(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        val textStyle: AozoraTextStyle,
        val indent: Int = 0,
        val maxCharacterPerLine: Int? = null,
    ) : AozoraBlock(blockIndex, elements) {
        val textCount: Int by lazy {
            elements.fold(0) { acc, element ->
                acc + element.length
            }
        }
    }

    data class Image(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
    ) : AozoraBlock(blockIndex, elements)

    val fullText: String by lazy {
        elements.fold("") { acc, element ->
            acc + element.debugText()
        }
    }

    fun copyWith(elements: List<AozoraElement>): AozoraBlock =
        when (this) {
            is Image -> this.copy(elements = elements)
            is TextBlock -> this.copy(elements = elements)
        }
}
