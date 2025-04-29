package com.andannn.aozora.core.service.di

import com.andannn.aozora.core.service.clientBuilder
import io.ktor.client.HttpClient
import org.koin.dsl.module

val serviceModule = module {
    single<HttpClient> {
        clientBuilder.invoke()
    }
}
