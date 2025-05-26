package me.andannn.aozora.ui.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Set immersive mode when this composable is active.
 * Set immersive mode off when this composable is disposed.
 */
@Composable
expect fun ImmersiveModeEffect(modifier: Modifier = Modifier)

/**
 * Set system ui visibility.
 */
@Composable
expect fun SystemUiVisibilityEffect(visible: Boolean)
