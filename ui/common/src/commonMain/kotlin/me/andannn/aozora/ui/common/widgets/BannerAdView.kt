/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun BannerAdView(
    modifier: Modifier = Modifier,
    adType: AdType = AdType.BANNER,
)

enum class AdType {
    LEADERBOARD,
    BANNER,
    MEDIUM_RECTANGLE,
    FULL_BANNER,
    LARGE_BANNER,
    WIDE_SKYSCRAPER,
}
