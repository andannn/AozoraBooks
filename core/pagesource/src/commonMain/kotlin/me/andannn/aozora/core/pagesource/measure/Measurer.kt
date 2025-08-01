/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.measure

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.core.domain.model.PageMetaData
import me.andannn.aozora.core.domain.model.PaperLayout
import me.andannn.aozora.core.domain.model.RenderSetting
import me.andannn.aozora.core.domain.model.resolveFontStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import kotlin.collections.get
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

internal data class ElementMeasureResult(
    val widthDp: Dp,
    val heightDp: Dp,
    val fontStyle: FontStyle? = null,
)

internal data class BlockMeasureResult(
    val lineCount: Int,
    val lineHeightDpPerLine: Dp,
    val availableTextCountPerLine: Int,
    val fontStyle: FontStyle? = null,
) {
    val totalLineHeightDp: Dp
        get() = lineHeightDpPerLine * lineCount
}

internal fun interface BlockMeasurer {
    fun measure(block: AozoraBlock): BlockMeasureResult
}

internal fun interface ElementMeasurer {
    fun measure(
        element: AozoraElement,
        style: AozoraTextStyle?,
    ): ElementMeasureResult
}

internal class DefaultMeasurer(
    private val renderSetting: RenderSetting,
    private val layout: PaperLayout,
) : ElementMeasurer,
    BlockMeasurer {
    constructor(meta: PageMetaData) : this(meta, meta)

    private val fontStyleCache = mutableMapOf<AozoraTextStyle, FontStyle>()
    private val renderHeight = layout.renderHeight

    override fun measure(
        element: AozoraElement,
        style: AozoraTextStyle?,
    ): ElementMeasureResult = sizeOf(element, style)

    override fun measure(block: AozoraBlock): BlockMeasureResult {
        when (block) {
            is AozoraBlock.Image -> return BlockMeasureResult(
                lineCount = 1,
                lineHeightDpPerLine = sizeOf(block.elements[0]).widthDp,
// TODO
                availableTextCountPerLine = 0,
            )

            is AozoraBlock.TextBlock -> {
                val style =
                    fontStyleCache[block.textStyle] ?: resolveAndSave(block.textStyle)
                val lineHeight = style.lineHeightDp

                if (block.elements.size == 1 && block.elements[0] is AozoraElement.LineBreak) {
                    val availableRenderHeight = renderHeight
                    val calculatedCountPerLine = floor(availableRenderHeight / style.baseSizeDp).toInt()
                    return BlockMeasureResult(
                        lineCount = 1,
                        lineHeightDpPerLine = lineHeight,
                        availableTextCountPerLine = calculatedCountPerLine,
                    )
                }

                val lineBreakCount = block.elements.count { it is AozoraElement.LineBreak }
                val plusLineNumber = (lineBreakCount - 1).coerceAtLeast(0)

                val indent = block.indent
                val indentHeight = style.baseSizeDp * indent
                val availableRenderHeight = renderHeight - indentHeight
                val calculatedCountPerLine = floor(availableRenderHeight / style.baseSizeDp).toInt()
                val availableTextCountPerLine =
                    if (block.maxCharacterPerLine == null) {
                        calculatedCountPerLine
                    } else {
                        min(calculatedCountPerLine, block.maxCharacterPerLine)
                    }
                return BlockMeasureResult(
                    lineCount = ceil(block.textCount.toFloat() / (availableTextCountPerLine)).toInt() + plusLineNumber,
                    lineHeightDpPerLine = lineHeight,
                    fontStyle = style,
                    availableTextCountPerLine = availableTextCountPerLine,
                )
            }
        }
    }

    private fun sizeOf(
        element: AozoraElement,
        aozoraStyle: AozoraTextStyle? = null,
    ): ElementMeasureResult {
        val cachedStyle = fontStyleCache[aozoraStyle]
        when (element) {
            is AozoraElement.BaseText -> {
                val style = cachedStyle ?: resolveAndSave(aozoraStyle!!)
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
                val style = cachedStyle ?: resolveAndSave(AozoraTextStyle.PARAGRAPH)
                return ElementMeasureResult(
                    fontStyle = style,
                    widthDp = style.baseSizeDp * style.lineHeightMultiplier,
                    heightDp = 0.dp,
                )
            }

            is AozoraElement.Indent -> {
                val style = cachedStyle ?: resolveAndSave(aozoraStyle!!)
                return ElementMeasureResult(
                    widthDp = style.lineHeightDp,
                    heightDp = style.baseSizeDp * element.count,
                )
            }

            AozoraElement.PageBreak -> error("error")
        }
    }

    private fun resolveAndSave(aozoraStyle: AozoraTextStyle): FontStyle {
        return aozoraStyle
            .resolveFontStyle(
                fontSizeLevel = renderSetting.fontSizeLevel,
                lineSpacing = renderSetting.lineSpacing,
                fontType = renderSetting.fontType,
            ).also {
                fontStyleCache[aozoraStyle] = it
                return it
            }
    }
}
