/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.di

import me.andannn.aozora.syncer.AozoraDBSyncer
import me.andannn.aozora.syncer.internal.AozoraDBSyncerImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val syncerModule: Module =
    module {
        singleOf(::AozoraDBSyncerImpl).bind(AozoraDBSyncer::class)
    }
