package me.andannn.aozora.core.pagesource.util

import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.BlockType

/**
 * Divide the element from [startIndex].
 * return null if this element can not be divided or [startIndex] is out of range.
 *
 * Return pair: [0, startIndex) -> [startIndex, length)
 */
fun AozoraElement.BaseText.divide(startIndex: Int): Pair<AozoraElement.BaseText, AozoraElement.BaseText>? {
    if (startIndex > length) {
        return null
    }
    return when (this) {
        is AozoraElement.Emphasis -> {
            val left =
                copy(
                    text = text.substring(0, startIndex),
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
                text = text.substring(0, startIndex),
            ) to
                copy(
                    text = text.substring(startIndex),
                )
        }

        is AozoraElement.Heading -> {
            error("Heading can not be divided")
        }

        else -> {
            null
        }
    }
}

/**
 * Divide the element by [startIndex] (not include).
 */
fun AozoraBlock.divideByTextIndex(startIndex: Int): Pair<AozoraBlock, AozoraBlock> {
    if (startIndex <= 0) {
        error("index must be greater than 0")
    }

    when (blockType) {
        BlockType.Image -> {
            error("Image can not be divided")
        }

        is BlockType.TextType -> {
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
                return this.copy(
                    elements = elements.subList(0, hitElement.index) + listOf(hitPair.first),
                ) to
                    this.copy(
                        elements =
                            listOf(hitPair.second) +
                                elements.subList(
                                    hitElement.index + 1,
                                    elements.size,
                                ),
                    )
            } else {
                val nextIndex = hitElement.index + 1
                return this.copy(
                    elements = elements.subList(0, nextIndex),
                ) to
                    this.copy(
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
