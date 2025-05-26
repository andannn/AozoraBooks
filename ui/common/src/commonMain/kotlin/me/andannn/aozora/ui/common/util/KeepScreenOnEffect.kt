/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.util

import androidx.compose.runtime.Composable

/**
 * Set keep screen on when this composable is active.
 * Set keep screen off when this composable is disposed.
 */
@Composable
expect fun KeepScreenOnEffect()
