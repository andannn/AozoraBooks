/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.platform

import platform.UIKit.UIViewController

class AdSizePx(
    val width: Float,
    val height: Float,
)

interface PlatformAdViewControllerFactory {
    fun generateBannerAdViewController(adType: AdType = AdType.BANNER): UIViewController

    fun getSizeOfAdType(adType: AdType): AdSizePx
}
