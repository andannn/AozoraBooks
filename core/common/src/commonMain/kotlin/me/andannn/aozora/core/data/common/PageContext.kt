/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

interface PaperLayout {
    /**
     * The original height of the page in pixels.
     */
    val originalHeight: Float

    /**
     * The original width of the page in pixels.
     */
    val originalWidth: Float

    /**
     * The render height of the page in pixels.
     */
    val renderHeight: Float

    /**
     * The render width of the page in pixels.
     */
    val renderWidth: Float

    /**
     * The offset from original view to render area.
     */
    val offset: Pair<Float, Float>
}

interface RenderSetting {
    /**
     * The font size level of the page.
     */
    val fontSizeLevel: FontSizeLevel

    /**
     * The line spacing of the page.
     */
    val lineSpacing: LineSpacing

    /**
     * The font type of the page.
     */
    val fontType: FontType

    /**
     * The top margin of the page.
     */
    val additionalTopMargin: TopMargin
}

interface PageMetaData :
    PaperLayout,
    RenderSetting

/**
 * Meta data for a render page.
 */
data class PageContext(
    override val additionalTopMargin: TopMargin = TopMargin.MEDIUM,
    override val fontType: FontType = FontType.DEFAULT,
    override val lineSpacing: LineSpacing = LineSpacing.MEDIUM,
    override val fontSizeLevel: FontSizeLevel = FontSizeLevel.LEVEL_4,
    override val originalHeight: Float,
    override val originalWidth: Float,
) : PageMetaData {
    override val offset: Pair<Float, Float> by lazy {
        Pair(
            // x =
            originalWidth * DEFAULT_HORIZONTAL_MARGIN_PERCENT / 2,
            // y =
            originalHeight * DEFAULT_VERTICAL_MARGIN_PERCENT / 2 + additionalTopMargin.value,
        )
    }

    override val renderHeight: Float by lazy {
        originalHeight * (1 - DEFAULT_VERTICAL_MARGIN_PERCENT) - additionalTopMargin.value
    }

    override val renderWidth: Float by lazy {
        originalWidth * (1 - DEFAULT_HORIZONTAL_MARGIN_PERCENT)
    }
}

private const val DEFAULT_HORIZONTAL_MARGIN_PERCENT = 0.15f
private const val DEFAULT_VERTICAL_MARGIN_PERCENT = 0.1f
