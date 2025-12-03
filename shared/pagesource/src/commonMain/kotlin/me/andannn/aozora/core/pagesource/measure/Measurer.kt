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
import me.andannn.aozora.core.domain.model.RenderSetting
import me.andannn.aozora.core.domain.model.resolveFontStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

internal data class BlockMeasureResult(
    val lineCount: Int,
    val lineHeightDpPerLine: Dp,
    val availableTextCountPerLine: Int,
    val fontStyle: FontStyle? = null,
) {
    val totalLineHeightDp: Dp
        get() = lineHeightDpPerLine * lineCount
}

internal fun interface BlockMeasureScope {
    fun measure(block: AozoraBlock): BlockMeasureResult
}

internal class BlockMeasureScopeImpl(
    private val renderHeight: Dp,
    private val textStyleCalculator: TextStyleCalculator,
) : BlockMeasureScope {
    override fun measure(block: AozoraBlock): BlockMeasureResult {
        when (block) {
            is AozoraBlock.Image -> return BlockMeasureResult(
                lineCount = 1,
// TODO
                lineHeightDpPerLine = 10.dp,
                availableTextCountPerLine = 0,
            )

            is AozoraBlock.TextBlock -> {
                val style = textStyleCalculator.resolve(block.textStyle)
                val lineHeight = style.lineHeightDp

                if (block.elements.size == 1 && block.elements[0] is AozoraElement.LineBreak) {
                    val availableRenderHeight = renderHeight
                    val calculatedCountPerLine =
                        floor(availableRenderHeight / style.baseSizeDp).toInt()
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
}

internal class TextStyleCalculator(
    private val renderSetting: RenderSetting,
) {
    private val fontStyleCache = mutableMapOf<AozoraTextStyle, FontStyle>()

    fun resolve(aozoraStyle: AozoraTextStyle): FontStyle {
        return fontStyleCache[aozoraStyle]
            ?: aozoraStyle
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
