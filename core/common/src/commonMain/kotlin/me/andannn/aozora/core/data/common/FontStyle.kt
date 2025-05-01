package me.andannn.aozora.core.data.common

data class FontStyle(
    /**
     * font type.
     */
    val fontType: FontType,
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
) {
    val lineHeight by lazy {
        baseSize * lineHeightMultiplier
    }
}

fun AozoraTextStyle.resolveFontStyle(
    fontSizeLevel: FontSizeLevel,
    lineSpacing: LineSpacing,
    fontType: FontType,
): FontStyle {
    val fontSize = fontSizeLevel.fontSize

    fun buildFontStyle(factor: Float): FontStyle {
        val baseSize = fontSize * factor
        val lineHeightMultiplier = lineSpacing.multiplier
        val notationSize = baseSize * (lineHeightMultiplier - 1).div(2)
        return FontStyle(
            baseSize = baseSize,
            notationSize = notationSize,
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
