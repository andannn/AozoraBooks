/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.Page
import me.andannn.aozora.core.domain.model.PageMetaData
import me.andannn.aozora.core.pagesource.measure.TextStyleCalculatorImpl

private const val TAG = "ReaderPageBuilder"

internal class LayoutPageBuilder constructor(
    private val meta: PageMetaData,
    private val textStyleCalculator: TextStyleCalculatorImpl =
        TextStyleCalculatorImpl(meta),
    private val forceAddBlock: Boolean = false,
) : PageBuilder<Page.LayoutPage> {
    private val fullWidth: Dp = meta.renderWidth
    private val fullHeight: Dp = meta.renderHeight

    private val lines = mutableListOf<Page.LayoutPage.LineWithBlockIndex>()

    private var currentWidth = 0.dp
    private var lineBuilder: LineBuilder? = null

    private var isPageBreakAdded = false
    private var currentBlockIndex = 0

    override fun tryAddBlock(block: AozoraBlock): FillResult =
        with(ElementMeasureScope(block, textStyleCalculator)) {
            Napier.v(tag = TAG) { "tryAddBlock E. block $block" }
            currentBlockIndex = block.blockIndex
            val remainingElements = block.elements.toMutableList()

            while (remainingElements.isNotEmpty()) {
                val element = remainingElements.first()
                val result =
                    tryAddElement(
                        element,
                        lineIndent = (block as? AozoraBlock.TextBlock)?.indent ?: 0,
                        maxCharacterPerLine = (block as? AozoraBlock.TextBlock)?.maxCharacterPerLine,
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

    private fun ElementMeasureScope.tryAddElement(
        element: AozoraElement,
        lineIndent: Int,
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
            val measureResult = measure(element)
            if (currentWidth + measureResult.widthDp > fullWidth) {
                return FillResult.Filled(element)
            }
        }

        val lineBuilder =
            lineBuilder ?: LineBuilder(
                maxDp = fullHeight,
                maxCharacterPerLine = maxCharacterPerLine,
            ).apply {
                if (lineIndent > 0) {
                    tryAdd(AozoraElement.Indent(lineIndent))
                }
            }.also {
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
                with(lineBuilder) {
                    when (val result = tryAdd(element)) {
                        FillResult.FillContinue -> {
                            return result
                        }

                        is FillResult.Filled -> {
                            buildNewLine()
                            val remainElement = result.remainElement
                            return if (remainElement == null) {
                                // The element is consumed by new line. return continue
                                FillResult.FillContinue
                            } else {
                                tryAddElement(remainElement, lineIndent, maxCharacterPerLine)
                            }
                        }
                    }
                }
            }

            AozoraElement.PageBreak,
            -> {
                error("Never")
            }
        }
    }

    override fun build(): Page.LayoutPage {
        if (lineBuilder != null) {
            buildNewLine()
        }

        return Page.LayoutPage(
            pageMetaData = meta,
            lines = lines.toImmutableList(),
        )
    }

    private fun buildNewLine() {
        val line = lineBuilder!!.build()
        lines += Page.LayoutPage.LineWithBlockIndex(line, currentBlockIndex)
        currentWidth += line.lineHeight
        lineBuilder = null
        Napier.v(tag = TAG) { "buildNewLine E. newLine $line, lines ${lines.size}, currentWidth $currentWidth" }
    }
}
