/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("ktlint:standard:filename")

package me.andannn.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import com.slack.circuit.retained.rememberRetained
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

@Stable
class StableCoroutineScope(
    scope: CoroutineScope,
) : CoroutineScope by scope

/**
 * Return a coroutine scope which bind lifecycle of backstack and survive configuration change.
 * Like lifecycle of ViewModelScope.
 *
 * Solution comes from:
 * https://github.com/chrisbanes/tivi/pull/1763
 */
@Composable
fun rememberRetainedCoroutineScope(): StableCoroutineScope =
    rememberRetained("coroutine_scope") {
        object : RememberObserver {
            val scope = StableCoroutineScope(CoroutineScope(Dispatchers.Main + Job()))

            override fun onAbandoned() = onForgotten()

            override fun onForgotten() {
                scope.cancel()
            }

            override fun onRemembered() = Unit
        }
    }.scope
