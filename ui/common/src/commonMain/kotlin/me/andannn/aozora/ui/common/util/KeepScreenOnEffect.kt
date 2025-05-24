package me.andannn.aozora.ui.common.util

import androidx.compose.runtime.Composable

internal const val TAG = "KeepScreenOnEffect"

/**
 * Set keep screen on when this composable is active.
 * Set keep screen off when this composable is disposed.
 */
@Composable
expect fun KeepScreenOnEffect()
