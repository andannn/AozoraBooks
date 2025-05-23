/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.LayoutPage
import me.andannn.aozora.core.data.common.Line
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.measure.ElementMeasureResult
import me.andannn.aozora.core.pagesource.measure.ElementMeasurer

private const val TAG = "ReaderPageBuilder"

internal class LayoutPageBuilder(
    private val meta: PageMetaData,
    private val measurer: ElementMeasurer,
    private val forceAddBlock: Boolean = false,
) {
    private val fullWidth: Dp = meta.renderWidth
    private val fullHeight: Dp = meta.renderHeight

    private val lines = mutableListOf<Line>()

    private var currentWidth = 0.dp
    private var lineBuilder: LineBuilder? = null

    private var isPageBreakAdded = false

    fun tryAddBlock(block: AozoraBlock): FillResult {
        Napier.v(tag = TAG) { "tryAddBlock E. block $block" }
        val remainingElements = block.elements.toMutableList()

        while (remainingElements.isNotEmpty()) {
            val element = remainingElements.first()
            val result =
                tryAddElement(
                    element,
                    lineIndent = (block as? AozoraBlock.TextBlock)?.indent ?: 0,
                    maxCharacterPerLine = (block as? AozoraBlock.TextBlock)?.maxCharacterPerLine,
                    sizeOf = { element ->
                        measurer.measure(
                            element,
                            (block as? AozoraBlock.TextBlock)?.textStyle,
                        )
                    },
                )

            when (result) {
                is FillResult.FillContinue -> {
                    remainingElements.removeAt(0) // 成功消费，移除
                }

                is FillResult.Filled -> {
                    remainingElements.removeAt(0) // 继续消费本 element
                    result.remainElement?.let { remainingElements.add(0, it) } // 还原未消费部分
                    break
                }
            }
        }

        return if (remainingElements.isEmpty()) {
            FillResult.FillContinue
        } else {
            FillResult.Filled(remainBlock = block.copyWith(remainingElements))
        }
    }

    fun tryAddElement(
        element: AozoraElement,
        lineIndent: Int,
        sizeOf: (AozoraElement) -> ElementMeasureResult,
        maxCharacterPerLine: Int? = null,
    ): FillResult {
        Napier.v(tag = TAG) { "tryAddElement E. element $element" }
        if (isPageBreakAdded) {
            return FillResult.Filled(element)
        }

        if (element is AozoraElement.PageBreak) {
            isPageBreakAdded = true
            return FillResult.FillContinue
        }

        if (!forceAddBlock && lineBuilder == null) {
            val measureResult = sizeOf(element)
            if (currentWidth + measureResult.widthDp > fullWidth) {
                return FillResult.Filled(element)
            }
        }

        val lineBuilder =
            lineBuilder ?: LineBuilder(
                maxDp = fullHeight,
                initialIndent = lineIndent,
                maxCharacterPerLine = maxCharacterPerLine,
                measure = sizeOf,
            ).also {
                lineBuilder = it
            }

        when (element) {
            is AozoraElement.Ruby,
            is AozoraElement.Text,
            is AozoraElement.LineBreak,
            is AozoraElement.Indent,
            is AozoraElement.Illustration,
            is AozoraElement.Emphasis,
            -> {
                when (val result = lineBuilder.tryAdd(element)) {
                    FillResult.FillContinue -> return result
                    is FillResult.Filled -> {
                        buildNewLine()
                        val remainElement = result.remainElement
                        return if (remainElement == null) {
                            // The element is consumed by new line. return continue
                            FillResult.FillContinue
                        } else {
                            tryAddElement(remainElement, lineIndent, sizeOf, maxCharacterPerLine)
                        }
                    }
                }
            }

            AozoraElement.PageBreak,
            is AozoraElement.Heading,
            is AozoraElement.SpecialParagraph,
            -> {
                error("Never")
            }
        }
    }

    fun build(): LayoutPage {
        if (lineBuilder != null) {
            buildNewLine()
        }

        return LayoutPage(
            pageMetaData = meta,
            lines = lines.toImmutableList(),
        )
    }

    private fun buildNewLine() {
        val line = lineBuilder!!.build()
        lines += line
        currentWidth += line.lineHeight
        lineBuilder = null
        Napier.v(tag = TAG) { "buildNewLine E. newLine $line, lines ${lines.size}, currentWidth $currentWidth" }
    }
}
