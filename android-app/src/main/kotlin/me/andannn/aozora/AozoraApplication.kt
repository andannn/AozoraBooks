/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora

import android.app.Application
import android.content.Context
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import me.andannn.platform.AndroidAnalytics
import org.koin.dsl.bind
import org.koin.dsl.module

class AozoraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }

        startKoin(
            analytics = AndroidAnalytics(),
            modules =
                listOf(
                    *modules.toTypedArray(),
                    androidContextModule(this@AozoraApplication),
                ),
        )
    }
}

private fun androidContextModule(application: AozoraApplication) =
    module {
        single { application } bind Context::class
    }
