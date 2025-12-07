/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.core.pagesource.measure.TextStyleCalculator

internal data class ElementMeasureResult(
    val widthDp: Dp,
    val heightDp: Dp,
    val fontStyle: FontStyle? = null,
)

internal interface ElementMeasureScope {
    fun measure(element: AozoraElement): ElementMeasureResult
}

internal fun ElementMeasureScope(
    block: AozoraBlock,
    textStyleCalculator: TextStyleCalculator,
): ElementMeasureScope = ElementMeasureScopeImpl(block, textStyleCalculator)

private class ElementMeasureScopeImpl(
    val block: AozoraBlock,
    val textStyleCalculator: TextStyleCalculator,
) : ElementMeasureScope {
    override fun measure(element: AozoraElement): ElementMeasureResult =
        when (val block = block) {
            is AozoraBlock.TextBlock -> {
                // For TextBlock, we can measure the element directly
                // using the provided measurer.
                measure(element, block.textStyle)
            }

            is AozoraBlock.Image -> {
                // For Image block, we might need to handle it differently,
                // but currently, we are not measuring images.
                measure(element, null)
            }
        }

    private fun measure(
        element: AozoraElement,
        aozoraStyle: AozoraTextStyle? = null,
    ): ElementMeasureResult {
        when (element) {
            is AozoraElement.BaseText -> {
                val style = textStyleCalculator.resolve(aozoraStyle!!)
                return ElementMeasureResult(
                    widthDp = style.lineHeightDp,
                    heightDp = style.baseSizeDp * element.length,
                    fontStyle = style,
                )
            }

            is AozoraElement.Illustration -> {
// TODO: support illustration element.
                return ElementMeasureResult(
                    widthDp = 0.dp,
                    heightDp = 0.dp,
                )
            }

            AozoraElement.LineBreak -> {
                val style = textStyleCalculator.resolve(AozoraTextStyle.PARAGRAPH)
                return ElementMeasureResult(
                    fontStyle = style,
                    widthDp = style.baseSizeDp * style.lineHeightMultiplier,
                    heightDp = 0.dp,
                )
            }

            is AozoraElement.Indent -> {
                val style = textStyleCalculator.resolve(AozoraTextStyle.PARAGRAPH)
                return ElementMeasureResult(
                    fontStyle = style,
                    widthDp = style.lineHeightDp,
                    heightDp = style.baseSizeDp * element.count,
                )
            }

            AozoraElement.PageBreak -> {
                error("error")
            }
        }
    }
}
