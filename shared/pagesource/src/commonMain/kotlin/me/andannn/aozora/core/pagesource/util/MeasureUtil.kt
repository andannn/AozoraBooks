/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.util

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.pagesource.page.AozoraBlock

internal data class DividedText(
    val left: AozoraElement.BaseText,
    val right: AozoraElement.BaseText,
)

/**
 * Divide the element from [startIndex].
 * throw error if [startIndex] is out of range.
 *
 * When [startIndex] <= 0 or >= length,  Return error
 * When [startIndex] in [1, length), Return pair: [0, startIndex) to [startIndex, length)
 */
internal fun AozoraElement.BaseText.divide(startIndex: Int): DividedText {
    if (startIndex !in 1..<length) error("startIndex must be in [1, $length) $startIndex")
    return when (this) {
        is AozoraElement.Emphasis -> {
            val left =
                copy(
                    text = text.take(startIndex),
                    style = style,
                )
            val right =
                copy(
                    text = text.substring(startIndex),
                    style = style,
                )
            DividedText(left, right)
        }

        is AozoraElement.Text -> {
            val left =
                copy(
                    text = text.take(startIndex),
                    style = style,
                )
            val right =
                copy(
                    text = text.substring(startIndex),
                    style = style,
                )
            DividedText(left, right)
        }

        is AozoraElement.Ruby -> {
            DividedText(AozoraElement.NoDividedLeftText, this)
        }
    }
}

/**
 * Divide block by line break.
 *
 * Heading or Special paragraph my have multiple line break. Divide them by line break.
 */
internal fun Sequence<AozoraBlock>.validBlock(): Sequence<AozoraBlock> =
    sequence {
        for (block in this@validBlock) {
            if (block.elements.count { it is AozoraElement.LineBreak } >= 2) {
                yieldAll(block.divideByLineBreak())
            } else {
                yield(block)
            }
        }
    }

private fun AozoraBlock.divideByLineBreak() =
    sequence<AozoraBlock> {
        var currentElements = mutableListOf<AozoraElement>()

        for (element in elements) {
            currentElements += element
            if (element is AozoraElement.LineBreak) {
                yield(this@divideByLineBreak.copyWith(elements = currentElements))
                currentElements = mutableListOf()
            }
        }
    }
