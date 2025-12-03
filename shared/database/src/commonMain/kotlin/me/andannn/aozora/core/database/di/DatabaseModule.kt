/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.di

import androidx.room.RoomDatabase
import me.andannn.aozora.core.database.AozoraDataBase
import me.andannn.aozora.core.database.dao.BookLibraryDao
import me.andannn.aozora.core.database.setUpDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val databaseBuilder: Module

val databaseModule =
    module {
        includes(
            databaseBuilder,
            module {
                single<AozoraDataBase> {
                    get<RoomDatabase.Builder<AozoraDataBase>>()
                        .setUpDatabase()
                        .build()
                }
                single<BookLibraryDao> { get<AozoraDataBase>().savedBookDao() }
            },
        )
    }
