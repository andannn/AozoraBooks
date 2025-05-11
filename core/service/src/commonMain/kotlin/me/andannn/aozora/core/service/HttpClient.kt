/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.service

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val clientBuilder: () -> HttpClient = {
    PlatformHttpClient.config {
        commonConfig()
    }
}

private fun HttpClientConfig<*>.commonConfig() {
    expectSuccess = true

    install(Resources)
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
            },
        )
    }
}
