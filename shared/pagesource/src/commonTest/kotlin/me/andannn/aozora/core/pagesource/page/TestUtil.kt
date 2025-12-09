/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.domain.model.Line
import kotlin.test.assertEquals

internal fun imageBlockOf(imageName: String) =
    AozoraBlock.Image(
        blockIndex = 0,
        image = AozoraElement.Illustration(imageName, null, null),
    )

internal fun textBlockOf(
    vararg textElements: AozoraElement,
    indent: Int = 0,
    maxCharacterPerLine: Int? = null,
): AozoraBlock.TextBlock =
    AozoraBlock.TextBlock(
        blockIndex = 0,
        textStyle = AozoraTextStyle.PARAGRAPH,
        indent = indent,
        maxCharacterPerLine = maxCharacterPerLine,
        elements = textElements.toList(),
    )

internal fun textBlockOf(
    vararg textElements: String,
    indent: Int = 0,
    maxCharacterPerLine: Int? = null,
): AozoraBlock.TextBlock =
    AozoraBlock.TextBlock(
        blockIndex = 0,
        textStyle = AozoraTextStyle.PARAGRAPH,
        indent = indent,
        maxCharacterPerLine = maxCharacterPerLine,
        elements =
            textElements.map { text ->
                AozoraElement.Text(text)
            },
    )

internal fun dummyElementMeasureScope(textSize: Dp) =
    object : ElementMeasureScope {
        override fun measure(element: AozoraElement): ElementMeasureResult {
            val height =
                when (element) {
                    is AozoraElement.BaseText -> {
                        element.length.times(textSize)
                    }

                    is AozoraElement.Indent -> {
                        textSize.times(element.count)
                    }

                    else -> {
                        0.dp
                    }
                }
            return ElementMeasureResult(
                widthDp = textSize,
                heightDp = height,
                fontStyle = null,
            )
        }
    }

internal fun text(text: String) = AozoraElement.Text(text)

internal fun image(name: String) = AozoraElement.Illustration(name, null, null)

internal fun ruby(text: String) = AozoraElement.Ruby(text, "")

internal fun Line.verify(vararg expects: String) {
    elements.forEachIndexed { index, element ->
        when (element) {
            is AozoraElement.Text -> {
                assertEquals(expects[index], element.text)
            }

            is AozoraElement.Indent -> {
                assertEquals(expects[index], "indent_${element.count}")
            }

            is AozoraElement.Ruby -> {
            }

            is AozoraElement.Illustration -> {
                assertEquals(expects[index], element.filename)
            }

            else -> {}
        }
    }
}
