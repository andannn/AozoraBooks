package me.andannn.aozora.core.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual val PlatformHttpClient: HttpClient = HttpClient(Darwin)
