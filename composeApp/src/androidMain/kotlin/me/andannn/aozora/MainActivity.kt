/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import me.andannn.aozora.app.AozoraBooksApp
import me.andannn.aozora.syncer.SyncWorkHelper
import me.andannn.aozora.ui.common.theme.AozoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        SyncWorkHelper.registerPeriodicSyncWork(this)

        setContent {
            AozoraTheme(
                dynamicColor = false,
            ) {
                AozoraBooksApp()
            }
        }
    }
}
