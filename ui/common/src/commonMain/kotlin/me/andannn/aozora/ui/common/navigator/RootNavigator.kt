/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.navigator

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.slack.circuit.runtime.Navigator

val RootNavigator: ProvidableCompositionLocal<Navigator> =
    compositionLocalOf { error("no popup controller") }
