/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.UIKitViewController
import me.andannn.platform.AdType
import me.andannn.platform.PlatformAdViewControllerFactory
import org.koin.mp.KoinPlatform.getKoin

@Composable
actual fun BannerAdView(
    modifier: Modifier,
    adType: AdType,
) {
    val factory =
        remember {
            getKoin().get<PlatformAdViewControllerFactory>()
        }
    val density = LocalDensity.current
    val height =
        with(density) {
            factory.getSizeOfAdType(adType).height.toDp()
        }
    UIKitViewController(
        modifier = modifier.fillMaxWidth().height(height),
        factory = {
            factory.generateBannerAdViewController()
        },
    )
}
