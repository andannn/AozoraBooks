/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import me.andannn.aozora.core.database.AozoraDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

const val DATABASE_FILE_NAME = "aozora_database.db"

internal actual val databaseBuilder: Module =
    module {
        single<RoomDatabase.Builder<AozoraDataBase>> {
            val appContext = androidContext().applicationContext
            Room
                .databaseBuilder<AozoraDataBase>(
                    context = appContext,
                    name = getKoin().get<Context>().getDatabasePath(DATABASE_FILE_NAME).absolutePath,
                ).setDriver(AndroidSQLiteDriver())
        }
    }
