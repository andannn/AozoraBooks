package me.andannn.aosora.common.parser.internal.util

import me.andannn.aosora.common.parser.AozoraElement

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
            val left = copy(
                text = text.substring(0, startIndex),
                style = style
            )
            val right = copy(
                text = text.substring(startIndex),
                style = style
            )
            left to right
        }

        is AozoraElement.Text -> {
            copy(
                text = text.substring(0, startIndex)
            ) to copy(
                text = text.substring(startIndex)
            )
        }

        is AozoraElement.Heading -> {
            // TODO: implement by divide
            copy(
            ) to copy(
            )
        }

        else -> {
            null
        }
    }
}
