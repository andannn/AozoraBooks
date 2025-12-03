/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.service.di

import io.ktor.client.HttpClient
import me.andannn.aozora.core.service.AozoraService
import me.andannn.aozora.core.service.clientBuilder
import me.andannn.aozora.core.service.impl.AozoraServiceImpl
import org.koin.dsl.module

val serviceModule =
    module {
        single<HttpClient> {
            clientBuilder.invoke()
        }

        single<AozoraService> {
            AozoraServiceImpl(get())
        }
    }
