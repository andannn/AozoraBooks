package me.andannn.aozora.core.database.di

import androidx.room.RoomDatabase
import me.andannn.aozora.core.database.AozoraDataBase
import me.andannn.aozora.core.database.dao.SavedBookDao
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
                single<SavedBookDao> { get<AozoraDataBase>().savedBookDao() }
            },
        )
    }
