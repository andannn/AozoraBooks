/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.di

import me.andannn.aozora.core.data.AozoraContentsRepositoryImpl
import me.andannn.aozora.core.data.UserDataRepositoryImpl
import me.andannn.aozora.core.database.di.databaseModule
import me.andannn.aozora.core.datastore.di.userPreferencesModule
import me.andannn.aozora.core.domain.layouthelper.AozoraPageLayoutHelper
import me.andannn.aozora.core.domain.pagesource.BookPageSource
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.core.pagesource.AozoraPageLayoutHelperImpl
import me.andannn.aozora.core.pagesource.BookPageSourceFactory
import me.andannn.aozora.core.service.di.serviceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule =
    module {
        singleOf(::UserDataRepositoryImpl).bind(UserDataRepository::class)
        singleOf(::AozoraContentsRepositoryImpl).bind(AozoraContentsRepository::class)
        singleOf(::BookPageSourceFactory).bind(BookPageSource.Factory::class)
        singleOf(::AozoraPageLayoutHelperImpl).bind(AozoraPageLayoutHelper::class)

        includes(
            serviceModule,
            userPreferencesModule,
            databaseModule,
        )
    }
