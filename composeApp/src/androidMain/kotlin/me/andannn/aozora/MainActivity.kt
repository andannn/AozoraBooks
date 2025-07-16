/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import me.andannn.aozora.app.AozoraBooksApp
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.syncer.AozoraDBSyncer
import me.andannn.aozora.syncer.SyncWorkHelper
import me.andannn.aozora.ui.common.theme.AozoraTheme
import org.koin.mp.KoinPlatform.getKoin

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        SyncWorkHelper.registerPeriodicSyncWork(this)

        lifecycleScope.launch {
            forceSyncAozoraDbIfNeeded()
        }

        setContent {
            AozoraTheme(
                dynamicColor = false,
            ) {
                AozoraBooksApp()
            }
        }
    }

    private suspend fun forceSyncAozoraDbIfNeeded() {
        val ndcTableMigrated = getKoin().get<UserDataRepository>().isNdcTableMigrated()

        if (!ndcTableMigrated) {
            Napier.d(tag = TAG) { "forceSyncAozora start." }
            val syncer = getKoin().get<AozoraDBSyncer>()
            syncer.sync(force = true)
        }
    }
}
