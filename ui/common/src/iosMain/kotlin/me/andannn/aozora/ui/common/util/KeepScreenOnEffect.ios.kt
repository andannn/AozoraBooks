package me.andannn.aozora.ui.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import io.github.aakira.napier.Napier
import platform.UIKit.UIApplication

@Composable
actual fun KeepScreenOnEffect() {
    LaunchedEffect(Unit) {
        Napier.d(tag = TAG) { "Keep screen on" }
        UIApplication.sharedApplication.setIdleTimerDisabled(true)
    }

    DisposableEffect(Unit) {
        onDispose {
            Napier.d(tag = TAG) { "Disable Keep screen on" }
            UIApplication.sharedApplication.setIdleTimerDisabled(false)
        }
    }
}
