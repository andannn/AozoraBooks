package me.andannn.core.util

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.remaining
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.readRemaining
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import okio.FileSystem
import okio.SYSTEM
import kotlin.use

/**
 *
 */
suspend fun HttpClient.downloadTo(
    url: String,
    path: Path
) {
    val response = get(url)
    val channel: ByteReadChannel = response.body()

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


private fun Path.createParentDirectories() {
    this.parent?.let { parent ->
        SystemFileSystem.createDirectories(parent)
    }
}