package me.andannn.aozora.core.data.di

import me.andannn.aozora.core.data.UserSettingRepository
import me.andannn.aozora.core.data.internal.UserSettingRepositoryImpl
import org.koin.dsl.module

val dataModule =
    module {
        single<UserSettingRepository> {
            UserSettingRepositoryImpl()
        }
    }
