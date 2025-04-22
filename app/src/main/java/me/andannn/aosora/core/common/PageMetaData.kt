package me.andannn.aosora.core.common

import android.graphics.PointF

/**
 * Meta data for a render page.
 */
data class PageMetaData(
    /**
     * The original height of the page in pixels.
     */
    val originalHeight: Float,

    /**
     * The original width of the page in pixels.
     */
    val originalWidth: Float,

    /**
     * The top margin of the page.
     */
    val additionalTopMargin: TopMargin = TopMargin.MEDIUM,

    /**
     * The font type of the page.
     */
    val fontType: FontType = FontType.DEFAULT,

    /**
     * The line spacing of the page.
     */
    val lineSpacing: LineSpacing = LineSpacing.MEDIUM,

    /**
     * The font size level of the page.
     */
    val fontSizeLevel: FontSizeLevel = FontSizeLevel.Level_4,
) {

    /**
     * The offset from original view to render area.
     */
    val offset: PointF by lazy {
        PointF(
            /* x = */
            originalWidth * DEFAULT_HORIZONTAL_MARGIN_PERCENT / 2,
            /* y = */
            originalHeight * DEFAULT_VERTICAL_MARGIN_PERCENT / 2 + additionalTopMargin.value,
        )
    }

    /**
     * The render height of the page in pixels.
     */
    val renderHeight: Float by lazy {
        originalHeight * (1 - DEFAULT_VERTICAL_MARGIN_PERCENT) - additionalTopMargin.value
    }

    /**
     * The render width of the page in pixels.
     */
    val renderWidth: Float by lazy {
        originalWidth * (1 - DEFAULT_HORIZONTAL_MARGIN_PERCENT)
    }
}

private const val DEFAULT_HORIZONTAL_MARGIN_PERCENT = 0.1f
private const val DEFAULT_VERTICAL_MARGIN_PERCENT = 0.05f