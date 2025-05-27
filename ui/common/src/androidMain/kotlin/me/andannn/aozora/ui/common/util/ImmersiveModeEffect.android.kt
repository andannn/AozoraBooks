/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import io.github.aakira.napier.Napier

private const val TAG = "ImmersiveModeEffect"

/**
 * Set immersive mode when this composable is active.
 * Set immersive mode off when this composable is disposed.
 */
@Composable
actual fun ImmersiveModeEffect(modifier: Modifier) {
    val activity = LocalContext.current as Activity
    LaunchedEffect(Unit) {
        val windowInsetsController =
            WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        Napier.d(tag = TAG) { "set systemBarsBehavior BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE" }
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
    DisposableEffect(Unit) {
        onDispose {
            Napier.d(tag = TAG) { "set systemBarsBehavior BEHAVIOR_DEFAULT" }
            val windowInsetsController =
                WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}

@Composable
actual fun SystemUiVisibilityEffect(visible: Boolean) {
    val activity = LocalContext.current as Activity
    LaunchedEffect(visible) {
        val windowInsetsController =
            WindowCompat.getInsetsController(activity.window, activity.window.decorView)

        if (visible) {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        } else {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}
