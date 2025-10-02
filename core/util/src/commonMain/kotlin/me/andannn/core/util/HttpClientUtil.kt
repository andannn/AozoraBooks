/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.core.util

import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.core.remaining
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.readRemaining
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 *
 */
suspend fun HttpClient.downloadFileTo(
    url: String,
    path: Path,
) {
    prepareGet(url).execute { response ->
        val channel = response.bodyAsChannel()

        path.createParentDirectories()
        val sink = SystemFileSystem.sink(path).buffered()
        sink.use {
            var count = 0L
            while (!channel.exhausted()) {
                val chunk = channel.readRemaining()
                count += chunk.remaining

                chunk.transferTo(it)
            }
        }
    }
}
