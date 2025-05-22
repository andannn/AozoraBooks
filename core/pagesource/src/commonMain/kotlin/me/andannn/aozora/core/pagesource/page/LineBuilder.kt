/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.Line
import me.andannn.aozora.core.pagesource.measure.ElementMeasureResult
import me.andannn.aozora.core.pagesource.util.divide

internal class LineBuilder(
    private val maxDp: Dp,
    initialIndent: Int = 0,
    private val measure: (AozoraElement) -> ElementMeasureResult,
) {
    private var currentHeight: Dp = 0.dp
    private var maxWidth: Dp = 0.dp
    private val elementList = mutableListOf<AozoraElement>()
    private var currentFontStyle: FontStyle? = null

    init {
        if (initialIndent > 0) {
            tryAdd(AozoraElement.Indent(initialIndent))
        }
    }

    fun tryAdd(element: AozoraElement): FillResult {
        when (element) {
            is AozoraElement.Ruby,
            is AozoraElement.Text,

            is AozoraElement.Emphasis,
            -> {
                val measureResult = measure(element)
                if (currentHeight + measureResult.heightDp > maxDp) {
                    val remainLength = maxDp - currentHeight
                    val singleTextHeight = measureResult.heightDp.div(element.length)
                    val remainSlot = remainLength.div(singleTextHeight).toInt()
                    if (remainSlot == 0) {
                        return FillResult.Filled(element)
                    } else {
                        element.divide(remainSlot)?.let {
                            val (left, right) = it
                            val leftResult = measure(left)

                            updateState(left, leftResult)
                            return FillResult.Filled(right)
                        } ?: return FillResult.Filled(element)
                    }
                }

                updateState(element, measureResult)
                return FillResult.FillContinue
            }

            AozoraElement.LineBreak -> {
                val measureResult = measure(element)
                updateState(element, measureResult)
                return FillResult.Filled()
            }

            is AozoraElement.Illustration,
            is AozoraElement.Indent,
            -> {
                if (elementList.isNotEmpty()) {
                    error("indent, and image can only be add to new line")
                } else {
                    val measureResult = measure(element)
                    updateState(element, measureResult)
                    return FillResult.FillContinue
                }
            }

            AozoraElement.PageBreak -> {
                error("Can not handle page break in line")
            }

            is AozoraElement.Heading,
            is AozoraElement.SpecialParagraph,
            -> {
                error("Never")
            }
        }
    }

    fun build(): Line =
        Line(
            lineHeight = maxWidth,
            elements = elementList.toImmutableList(),
            fontStyle = currentFontStyle,
        )

    private fun updateState(
        element: AozoraElement,
        measureResult: ElementMeasureResult,
    ) {
        elementList += element
        currentHeight += measureResult.heightDp
        maxWidth = maxOf(maxWidth, measureResult.widthDp)
        if (measureResult.fontStyle != null) {
            currentFontStyle = measureResult.fontStyle
        }
    }
}
