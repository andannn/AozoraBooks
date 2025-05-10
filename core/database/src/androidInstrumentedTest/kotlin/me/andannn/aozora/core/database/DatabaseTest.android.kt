package me.andannn.aozora.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider

internal actual fun inMemoryDatabaseBuilder(): RoomDatabase.Builder<AozoraDataBase> =
    Room
        .inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AozoraDataBase::class.java,
        ).allowMainThreadQueries()
