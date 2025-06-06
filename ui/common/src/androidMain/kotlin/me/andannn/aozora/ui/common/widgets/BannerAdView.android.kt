/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import me.andannn.platform.AdType

@RequiresPermission(android.Manifest.permission.INTERNET)
@Composable
actual fun BannerAdView(
    modifier: Modifier,
    adType: AdType,
) {
    val context = LocalContext.current
    val adSize = adType.toSize()
    val adView by produceState<AdView?>(null) {
        value =
            AdView(context)
                .apply {
                    setAdSize(adSize)
                    adUnitId = "ca-app-pub-9577779442408289/8866179143"
                }.also { adView ->
                    adView.loadAd(AdRequest.Builder().build())
                }
    }
    DisposableEffect(Unit) {
        onDispose {
            adView?.destroy()
        }
    }
    Box(
        modifier = modifier.height(adSize.height.dp),
    ) {
        if (adView != null) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = {
                    adView!!
                },
            )
        }
    }
}

private fun AdType.toSize() =
    when (this) {
        AdType.BANNER -> AdSize.BANNER
        AdType.FULL_BANNER -> AdSize.FULL_BANNER
        AdType.LARGE_BANNER -> AdSize.LARGE_BANNER
        AdType.LEADERBOARD -> AdSize.LEADERBOARD
        AdType.MEDIUM_RECTANGLE -> AdSize.MEDIUM_RECTANGLE
        AdType.WIDE_SKYSCRAPER -> AdSize.WIDE_SKYSCRAPER
    }
