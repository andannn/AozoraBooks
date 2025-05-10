/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.di

import me.andannn.aozora.core.data.AozoraContentsRepository
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.internal.AozoraContentsRepositoryImpl
import me.andannn.aozora.core.data.internal.UserDataRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule =
    module {
        singleOf(::UserDataRepositoryImpl).bind(UserDataRepository::class)
        singleOf(::AozoraContentsRepositoryImpl).bind(AozoraContentsRepository::class)
    }
