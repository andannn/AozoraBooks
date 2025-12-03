/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.andannn.platform.AdType

@Composable
expect fun BannerAdView(
    modifier: Modifier = Modifier,
    adType: AdType = AdType.BANNER,
)
