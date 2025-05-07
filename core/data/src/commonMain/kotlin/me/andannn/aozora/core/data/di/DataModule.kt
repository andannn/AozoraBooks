package me.andannn.aozora.core.data.di

import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.internal.UserDataRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule =
    module {
        singleOf(::UserDataRepositoryImpl).bind(UserDataRepository::class)
    }
