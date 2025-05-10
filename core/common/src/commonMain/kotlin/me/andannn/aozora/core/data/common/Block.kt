/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

sealed class Block(
    open val blockIndex: Int,
    open val elements: List<AozoraElement>,
) {
    sealed class TextBlock(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        open val textStyle: AozoraTextStyle,
        open val indent: Int = 0,
    ) : Block(blockIndex, elements)

    data class Heading(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        override val textStyle: AozoraTextStyle,
        override val indent: Int,
    ) : TextBlock(blockIndex, elements, textStyle)

    data class Paragraph(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
    ) : TextBlock(blockIndex, elements, AozoraTextStyle.PARAGRAPH, 0)

    data class Image(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
    ) : Block(blockIndex, elements)

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

    fun copyWith(elements: List<AozoraElement>): Block =
        when (this) {
            is Heading -> this.copy(elements = elements)
            is Image -> this.copy(elements = elements)
            is Paragraph -> this.copy(elements = elements)
        }
}
