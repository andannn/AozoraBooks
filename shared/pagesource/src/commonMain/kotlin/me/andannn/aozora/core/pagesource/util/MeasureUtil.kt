/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.util

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.pagesource.page.AozoraBlock

/**
 * Divide the element from [startIndex].
 * return null if this element can not be divided or [startIndex] is out of range.
 *
 * Return pair: [0, startIndex) -> [startIndex, length)
 */
internal fun AozoraElement.BaseText.divide(startIndex: Int): Pair<AozoraElement.BaseText, AozoraElement.BaseText>? {
    if (startIndex <= 0) error("startIndex must be greater than 0. startIndex $startIndex")
    if (startIndex > length) {
        return null
    }
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
            left to right
        }

        is AozoraElement.Text -> {
            copy(
                text = text.take(startIndex),
            ) to
                copy(
                    text = text.substring(startIndex),
                )
        }

        else -> {
            null
        }
    }
}

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
internal fun AozoraElement.BaseText.divide2(startIndex: Int): DividedText {
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

/**
 * Divide the element by [startIndex] (not include).
 */
internal fun AozoraBlock.divideByTextIndex(startIndex: Int): Pair<AozoraBlock, AozoraBlock> {
    if (startIndex <= 0) {
        error("index must be greater than 0. startIndex $startIndex")
    }

    when (this) {
        is AozoraBlock.Image -> {
            error("Image can not be divided")
        }

        is AozoraBlock.TextBlock -> {
            var currentElementStartIndex = 0
            var hitElement: IndexedValue<AozoraElement>? = null
            for ((index, element) in elements.withIndex()) {
                if (currentElementStartIndex + element.length >= startIndex) {
                    hitElement = IndexedValue(index, element)
                    break
                }

                currentElementStartIndex += element.length
            }

            if (hitElement == null) {
                error("no element found")
            }

            var hitPair: Pair<AozoraElement.BaseText, AozoraElement.BaseText>? = null
            (hitElement.value as? AozoraElement.BaseText)
                ?.divide(startIndex - currentElementStartIndex)
                ?.let {
                    hitPair = it
                }
            if (hitPair != null) {
                return this.copyWith(
                    elements = elements.subList(0, hitElement.index) + listOf(hitPair.first),
                ) to
                    this.copyWith(
                        elements =
                            listOf(hitPair.second) +
                                elements.subList(
                                    hitElement.index + 1,
                                    elements.size,
                                ),
                    )
            } else {
                val nextIndex = hitElement.index + 1
                return this.copyWith(
                    elements = elements.subList(0, nextIndex),
                ) to
                    this.copyWith(
                        elements =
                            elements.subList(
                                nextIndex,
                                elements.size,
                            ),
                    )
            }
        }
    }
}

internal data class DividedBlock(
    val left: AozoraBlock,
    val right: AozoraBlock,
)

/**
 * Divide the block from [startIndex].
 * Throw error if [startIndex] is out of range.
 *
 * Valid range: [1, textCount)
 * - When [startIndex] <= 0 or >= textCount → error
 * - When [startIndex] ∈ [1, textCount) →
 *   return pair: [0, startIndex) and [startIndex, textCount)
 */
internal fun AozoraBlock.TextBlock.divideByTextIndex2(startIndex: Int): DividedBlock {
    require(startIndex in 1 until textCount) {
        "startIndex must be in [1, $textCount), actual: $startIndex"
    }

    var currentElementStartIndex = 0

    elements.forEachIndexed { index, element ->
        val elementEndIndex = currentElementStartIndex + element.length

        when {
            startIndex == elementEndIndex -> {
                val splitIndex = index + 1
                return DividedBlock(
                    left =
                        copyWith(
                            elements = elements.subList(0, splitIndex),
                        ),
                    right =
                        copyWith(
                            elements = elements.subList(splitIndex, elements.size),
                        ),
                )
            }

            startIndex < elementEndIndex -> {
                val relativeIndex = startIndex - currentElementStartIndex
                val baseText = element as AozoraElement.BaseText

                val divided: DividedText = baseText.divide2(relativeIndex)

                val (leftText, rightText) = divided
                val left =
                    copyWith(
                        elements = elements.subList(0, index) + listOf(leftText),
                    )
                val right =
                    copyWith(
                        elements =
                            listOf(rightText) +
                                elements.subList(index + 1, elements.size),
                    )
                return DividedBlock(left, right)
            }
        }

        currentElementStartIndex = elementEndIndex
    }

    error("No element found for startIndex=$startIndex in [1, $textCount)")
}
