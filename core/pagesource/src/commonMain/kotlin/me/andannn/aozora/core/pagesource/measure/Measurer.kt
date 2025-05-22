/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.measure

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.PaperLayout
import me.andannn.aozora.core.data.common.RenderSetting
import me.andannn.aozora.core.data.common.resolveFontStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import kotlin.collections.get
import kotlin.math.ceil
import kotlin.math.floor

data class ElementMeasureResult(
    val widthDp: Dp,
    val heightDp: Dp,
    val fontStyle: FontStyle? = null,
)

data class BlockMeasureResult(
    val lineCount: Int,
    val lineHeightDpPerLine: Dp,
    val availableRenderHeightDp: Dp,
    val fontStyle: FontStyle? = null,
) {
    val totalLineHeightDp: Dp
        get() = lineHeightDpPerLine * lineCount
}

internal fun interface BlockMeasurer {
    fun measure(block: AozoraBlock): BlockMeasureResult
}

fun interface ElementMeasurer {
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
                availableRenderHeightDp = renderHeight,
            )

            is AozoraBlock.TextBlock -> {
                val style =
                    fontStyleCache[block.textStyle] ?: resolveAndSave(block.textStyle)
                val lineHeight = style.lineHeightDp

                if (block.elements.size == 1 && block.elements[0] is AozoraElement.LineBreak) {
                    return BlockMeasureResult(
                        lineCount = 1,
                        lineHeightDpPerLine = lineHeight,
                        availableRenderHeightDp = renderHeight,
                    )
                }

                val lineBreakCount = block.elements.count { it is AozoraElement.LineBreak }
                val plusLineNumber = (lineBreakCount - 1).coerceAtLeast(0)

                val indent = block.indent
                val indentHeight = style.baseSizeDp * indent
                val availableRenderHeight = renderHeight - indentHeight
                val availableTextCountPerLine =
                    floor(availableRenderHeight / style.baseSizeDp).toInt()
                return BlockMeasureResult(
                    lineCount = ceil(block.textCount.toFloat() / (availableTextCountPerLine)).toInt() + plusLineNumber,
                    lineHeightDpPerLine = lineHeight,
                    fontStyle = style,
                    availableRenderHeightDp = availableRenderHeight,
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
                error("")
//                return ElementMeasureResult(
//                    widthDp =
//                        element.width?.toFloat() ?: 0f,
//                    heightDp = element.height?.toFloat() ?: 0f,
//                )
            }

            AozoraElement.LineBreak -> {
                // TODO: Line break element can be measured ?
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
