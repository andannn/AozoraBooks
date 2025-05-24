package me.andannn.aozora.ui.common.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import io.github.aakira.napier.Napier

@Composable
actual fun KeepScreenOnEffect() {
    val activity = LocalContext.current as Activity
    LaunchedEffect(Unit) {
        Napier.d(tag = TAG) { "Keep screen on" }
        activity.window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    DisposableEffect(Unit) {
        onDispose {
            Napier.d(tag = TAG) { "Disable Keep screen on" }
            activity.window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
