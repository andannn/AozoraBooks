package me.andannn.aosora.core.common

import me.andannn.aosora.core.parser.AozoraTextStyle

data class FontStyle(
    /**
     * base font size in pixels.
     */
    val baseSize: Float,

    /**
     * notation font size in pixels.
     */
    val notationSize: Float,

    /**
     * line height multiplier.
     */
    val lineHeightMultiplier: Float,
)

fun AozoraTextStyle.resolveFontStyle(
    fontSizeLevel: FontSizeLevel,
    lineSpacing: LineSpacing
): FontStyle {
    val scale = fontSizeLevel.fontScaleFactor
    val base = 24f

    fun buildFontStyle(factor: Float): FontStyle {
        val baseSize = base * scale * factor
        val notationSize = baseSize * 0.6f
        val lineHeightMultiplier = lineSpacing.multiplier
        return FontStyle(baseSize, notationSize, lineHeightMultiplier)
    }

    return when (this) {
        AozoraTextStyle.PARAGRAPH -> buildFontStyle(1.0f)
        AozoraTextStyle.HEADING_SMALL -> buildFontStyle(1.2f)
        AozoraTextStyle.HEADING_MEDIUM -> buildFontStyle(1.5f)
        AozoraTextStyle.HEADING_LARGE -> buildFontStyle(1.8f)
    }
}