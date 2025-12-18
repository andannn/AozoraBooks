/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidDatabaseTest : AbstractDatabaseTest() {
    override val inMemoryDatabaseBuilder: RoomDatabase.Builder<AozoraDataBase> =
        Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AozoraDataBase::class.java,
            ).allowMainThreadQueries()
}
