package com.andannn.aozora.core.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

internal actual val PlatformHttpClient: HttpClient = HttpClient(CIO)