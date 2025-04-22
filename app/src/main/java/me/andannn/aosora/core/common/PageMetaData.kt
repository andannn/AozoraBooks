package me.andannn.aosora.core.common

/**
 * Meta data for a render page.
 */
data class PageMetaData constructor(
    /**
     * The height of the page in pixels.
     */
    val renderHeight: Float,

    /**
     * The width of the page in pixels.
     */
    val renderWidth: Float,

    /**
     * The font type of the page.
     */
    val fontType: FontType,

    /**
     * The line spacing of the page.
     */
    val lineSpacing: LineSpacing = LineSpacing.MEDIUM,

    /**
     * The font size level of the page.
     */
    val fontSizeLevel: FontSizeLevel = FontSizeLevel.Level_4,
)