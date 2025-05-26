/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Set immersive mode when this composable is active.
 * Set immersive mode off when this composable is disposed.
 */
@Composable
actual fun ImmersiveModeEffect(modifier: Modifier) {
}

@Composable
actual fun SystemUiVisibilityEffect(visible: Boolean) {
}
