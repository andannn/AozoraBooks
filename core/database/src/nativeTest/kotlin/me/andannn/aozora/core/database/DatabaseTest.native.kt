package me.andannn.aozora.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

internal actual fun inMemoryDatabaseBuilder(): RoomDatabase.Builder<AozoraDataBase> =
    Room
        .inMemoryDatabaseBuilder<AozoraDataBase>()
        .setDriver(BundledSQLiteDriver())
