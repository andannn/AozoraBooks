/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora

import me.andannn.platform.PlatformAdViewControllerFactory
import org.koin.core.module.Module
import org.koin.dsl.module

fun adViewControllerFactoryModule(adViewControllerFactory: PlatformAdViewControllerFactory): Module =
    module {
        single<PlatformAdViewControllerFactory> {
            adViewControllerFactory
        }
    }
