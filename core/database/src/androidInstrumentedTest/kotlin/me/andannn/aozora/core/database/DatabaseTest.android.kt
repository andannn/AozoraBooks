/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.core.app.ApplicationProvider

actual val inMemoryDatabaseBuilder: RoomDatabase.Builder<AozoraDataBase> =
    Room
        .inMemoryDatabaseBuilder<AozoraDataBase>(
            ApplicationProvider.getApplicationContext(),
        ).setDriver(AndroidSQLiteDriver())

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class IgnoreAndroidUnitTest actual constructor()

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class IgnoreNativeTest actual constructor()
