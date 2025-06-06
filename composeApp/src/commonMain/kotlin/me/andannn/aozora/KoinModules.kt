/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora

import me.andannn.aozora.core.data.di.dataModule
import me.andannn.aozora.syncer.di.syncerModule
import org.koin.core.module.Module

val modules: List<Module> =
    listOf(
        dataModule,
        syncerModule,
    )
