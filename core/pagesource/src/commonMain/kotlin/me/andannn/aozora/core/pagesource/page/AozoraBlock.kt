/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.data.common.Block
import me.andannn.aozora.core.data.common.debugText

internal sealed class AozoraBlock(
    override val blockIndex: Int,
    open val elements: List<AozoraElement>,
) : Block {
    sealed class TextBlock(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        open val textStyle: AozoraTextStyle,
        open val indent: Int = 0,
        open val maxTextLength: Int = Int.MAX_VALUE,
    ) : AozoraBlock(blockIndex, elements)

    data class Heading(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        override val textStyle: AozoraTextStyle,
        override val indent: Int,
    ) : TextBlock(blockIndex, elements, textStyle)

    data class Paragraph(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        override val indent: Int = 0,
        override val maxTextLength: Int = Int.MAX_VALUE,
    ) : TextBlock(blockIndex, elements, AozoraTextStyle.PARAGRAPH, 0)

    data class Image(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
    ) : AozoraBlock(blockIndex, elements)

    val textCount: Int by lazy {
        elements.fold(0) { acc, element ->
            acc + element.length
        }
    }

    val fullText: String by lazy {
        elements.fold("") { acc, element ->
            acc + element.debugText()
        }
    }

    fun copyWith(elements: List<AozoraElement>): AozoraBlock =
        when (this) {
            is Heading -> this.copy(elements = elements)
            is Image -> this.copy(elements = elements)
            is Paragraph -> this.copy(elements = elements)
        }
}
