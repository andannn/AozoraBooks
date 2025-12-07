/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp

interface PaperLayout {
    /**
     * The original height of the page in pixels.
     */
    val originalHeight: Dp

    /**
     * The original width of the page in pixels.
     */
    val originalWidth: Dp

    /**
     * The render height of the page in pixels.
     */
    val renderHeight: Dp

    /**
     * The render width of the page in pixels.
     */
    val renderWidth: Dp

    /**
     * The offset from original view to render area.
     */
    val offset: Pair<Dp, Dp>
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

@Stable
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
    override val originalHeight: Dp,
    override val originalWidth: Dp,
) : PageMetaData {
    override val offset: Pair<Dp, Dp> by lazy {
        Pair(
            // x =
            originalWidth * DEFAULT_HORIZONTAL_MARGIN_PERCENT / 2,
            // y =
            originalHeight * DEFAULT_TOP_MARGIN_PERCENT + additionalTopMargin.value,
        )
    }

    override val renderHeight: Dp by lazy {
        originalHeight * (1 - DEFAULT_TOP_MARGIN_PERCENT - DEFAULT_BOTTOM_MARGIN_PERCENT) - additionalTopMargin.value
    }

    override val renderWidth: Dp by lazy {
        originalWidth * (1 - DEFAULT_HORIZONTAL_MARGIN_PERCENT)
    }
}

private const val DEFAULT_HORIZONTAL_MARGIN_PERCENT = 0.15f
private const val DEFAULT_TOP_MARGIN_PERCENT = 0.05f
private const val DEFAULT_BOTTOM_MARGIN_PERCENT = 0.02f
