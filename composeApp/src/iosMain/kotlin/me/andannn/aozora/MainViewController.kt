/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import me.andannn.aozora.app.AozoraBooksApp
import me.andannn.aozora.syncer.AozoraDBSyncer
import me.andannn.aozora.syncer.SyncResult
import me.andannn.aozora.ui.common.theme.AozoraTheme
import org.koin.mp.KoinPlatform.getKoin

@Suppress("ktlint:standard:function-naming")
fun MainViewController() =
    ComposeUIViewController {
        LaunchedEffect(Dispatchers.Default) {
            val syncer = getKoin().get<AozoraDBSyncer>()
            val result = syncer.sync()
            when (result) {
                SyncResult.Retry -> {
                    // Only retry once.
                    syncer.sync()
                }

                is SyncResult.Fail,
                SyncResult.Success,
                -> {
                    // NOOP
                }
            }
        }

        AozoraTheme {
            AozoraBooksApp()
        }
    }

fun enableDebugLog() {
    Napier.base(DebugAntilog())
}
