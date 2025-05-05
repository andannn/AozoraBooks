package me.andannn.aozora.core.data.di

import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.internal.UserDataRepositoryImpl
import org.koin.dsl.module

val dataModule =
    module {
        single<UserDataRepository> {
            UserDataRepositoryImpl()
        }
    }
