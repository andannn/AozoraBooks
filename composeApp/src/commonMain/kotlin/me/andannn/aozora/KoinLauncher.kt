/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora

import me.andannn.platform.PlatformAnalytics
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun startKoin(
    analytics: PlatformAnalytics,
    modules: List<Module> = emptyList(),
) {
    startKoin {
        modules(modules)
        modules(platformModules(analytics))
    }
}

private fun platformModules(analytics: PlatformAnalytics): List<Module> =
    listOf(
        analyticsModule(analytics),
    )

private fun analyticsModule(analytics: PlatformAnalytics): Module =
    module {
        single<PlatformAnalytics> {
            analytics
        }
    }
