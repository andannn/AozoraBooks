/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

class NativeDatabaseTest : AbstractDatabaseTest() {
    override val inMemoryDatabaseBuilder: RoomDatabase.Builder<AozoraDataBase> =
        Room
            .inMemoryDatabaseBuilder<AozoraDataBase>()
            .setDriver(BundledSQLiteDriver())
}
