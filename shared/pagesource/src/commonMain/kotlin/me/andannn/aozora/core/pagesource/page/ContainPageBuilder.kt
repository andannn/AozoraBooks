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

@Suppress("ktlint:standard:function-naming")
internal fun ContentPageBuilder(meta: PageMetaData) =
    ContainPageBuilder(
        meta.renderWidth,
        meta.renderHeight,
        scopeBuilder = {
            ElementMeasureScope(it, TextStyleCalculatorImpl(meta))
        },
    )

internal class ContainPageBuilder(
    private val fullWidth: Dp,
    private val fullHeight: Dp,
    private val forceAddBlock: Boolean = false,
    private val scopeBuilder: (block: AozoraBlock) -> ElementMeasureScope,
) : PageBuilder<Page.ContentPage> {
    private val lines = mutableListOf<Page.TextLayoutPage.LineWithBlockIndex>()

    private var currentWidth = 0.dp
    private var lineBuilder: LineBuilder? = null

    private var isPageBreakAdded = false
    private var currentBlockIndex = 0
    private var addedImageElement: AozoraElement.Illustration? = null

    override fun tryAddBlock(block: AozoraBlock): FillResult =
        with(scopeBuilder(block)) {
            Napier.v(tag = TAG) { "tryAddBlock E. block $block" }
            currentBlockIndex = block.blockIndex

            if (addedImageElement != null) {
                return FillResult.Filled(remainBlock = block)
            }

            when (block) {
                is AozoraBlock.Image -> {
                    if (isEmpty()) {
                        addedImageElement = block.image
                        return FillResult.Filled()
                    } else {
                        // illustration can only be added to new page.
                        return FillResult.Filled(remainBlock = block)
                    }
                }

                is AozoraBlock.TextBlock -> {
                    val remainingElements = block.elements.toMutableList()

                    while (remainingElements.isNotEmpty()) {
                        val element = remainingElements.first()
                        val result =
                            tryAddElement(
                                element = element,
                                lineIndent = block.indent,
                                maxCharacterPerLine = block.maxCharacterPerLine,
                            )

                        when (result) {
                            is FillResult.FillContinue -> {
                                remainingElements.removeAt(0)
                            }

                            is FillResult.Filled -> {
                                remainingElements.removeAt(0)
                                result.remainElement?.let { remainingElements.add(0, it) }
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
            }
        }

    private fun ElementMeasureScope.tryAddElement(
        element: AozoraElement,
        lineIndent: Int,
        maxCharacterPerLine: Int? = null,
    ): FillResult {
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

            is AozoraElement.Illustration -> {
                error("Never")
            }

            AozoraElement.PageBreak -> {
                error("Never")
            }
        }
    }

    override fun build(): Page.ContentPage {
        if (addedImageElement != null) {
            return Page.ImagePage(
                element = addedImageElement!!,
                contentWidth = fullWidth,
                elementIndex = currentBlockIndex,
            )
        }

        if (lineBuilder != null) {
            buildNewLine()
        }

        return Page.TextLayoutPage(
            lines = lines.toImmutableList(),
        )
    }

    private fun isEmpty() = lines.isEmpty() && (lineBuilder == null || lineBuilder?.isEmpty() == true)

    private fun buildNewLine() {
        val line = lineBuilder!!.build()
        lines += Page.TextLayoutPage.LineWithBlockIndex(line, currentBlockIndex)
        currentWidth += line.lineHeight
        lineBuilder = null
        Napier.v(tag = TAG) { "buildNewLine E. newLine $line, lines ${lines.size}, currentWidth $currentWidth" }
    }
}
