package me.andannn.aosora.core.measure

import me.andannn.aosora.core.common.FontSizeLevel
import me.andannn.aosora.core.common.LineSpacing

/**
 * Meta data for a render page.
 */
data class PageMetaData(
    /**
     * The height of the page in pixels.
     */
    val renderHeight: Float,

    /**
     * The width of the page in pixels.
     */
    val renderWidth: Float,

    /**
     * The line spacing of the page.
     */
    val lineSpacing: LineSpacing = LineSpacing.MEDIUM,

    /**
     * The font size level of the page.
     */
    val fontSizeLevel: FontSizeLevel = FontSizeLevel.Level_4,
)