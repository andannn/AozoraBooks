/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import androidx.compose.ui.unit.Dp

data class FontStyle(
    /**
     * font type.
     */
    val fontType: FontType,
    /**
     * base font size in display size(dp).
     */
    val baseSizeDp: Dp,
    /**
     * notation font size in display size(dp).
     */
    val notationSizeDp: Dp,
    /**
     * line height multiplier.
     */
    val lineHeightMultiplier: Float,
) {
    val lineHeightDp by lazy {
        baseSizeDp * lineHeightMultiplier
    }
}

fun AozoraTextStyle.resolveFontStyle(
    fontSizeLevel: FontSizeLevel,
    lineSpacing: LineSpacing,
    fontType: FontType,
): FontStyle {
    val fontSize = fontSizeLevel.fontSizeDp

    fun buildFontStyle(factor: Float): FontStyle {
        val baseSize = fontSize * factor
        val lineHeightMultiplier = lineSpacing.multiplier
        val notationSize = baseSize * (lineHeightMultiplier - 1).div(2)
        return FontStyle(
            baseSizeDp = baseSize,
            notationSizeDp = notationSize,
            lineHeightMultiplier = lineHeightMultiplier,
            fontType = fontType,
        )
    }

    return when (this) {
        AozoraTextStyle.PARAGRAPH -> buildFontStyle(1.0f)
        AozoraTextStyle.HEADING_SMALL -> buildFontStyle(1.2f)
        AozoraTextStyle.HEADING_MEDIUM -> buildFontStyle(1.5f)
        AozoraTextStyle.HEADING_LARGE -> buildFontStyle(1.8f)
    }
}
