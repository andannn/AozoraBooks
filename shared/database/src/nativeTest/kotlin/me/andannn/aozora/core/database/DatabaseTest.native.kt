/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual val inMemoryDatabaseBuilder: RoomDatabase.Builder<AozoraDataBase> =
    Room
        .inMemoryDatabaseBuilder<AozoraDataBase>()
        .setDriver(BundledSQLiteDriver())

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class IgnoreAndroidUnitTest actual constructor()

actual typealias IgnoreNativeTest = kotlin.test.Ignore
