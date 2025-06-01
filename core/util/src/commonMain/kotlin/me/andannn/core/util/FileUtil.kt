/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.core.util

import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer
import okio.openZip
import okio.use


/**
 * Write a [Source] to a [Path].
 */
fun Source.writeToPath(path: Path) {
    path.parent?.let { SystemFileSystem.createDirectories(it) }

    SystemFileSystem.sink(path).buffered().use { sink ->
        this.use { source ->
            val buffer = ByteArray(8192)
            while (true) {
                val bytesRead = source.readAtMostTo(buffer, 0, buffer.size)
                when {
                    bytesRead == -1 -> break
                    bytesRead > 0 -> sink.write(buffer, 0, bytesRead)
                    else -> continue // bytesRead == 0, non-blocking case
                }
            }
        }
    }
}

fun Path.createParentDirectories() {
    this.parent?.let { parent ->
        SystemFileSystem.createDirectories(parent)
    }
}

/**
 * Unzip a zip file to a target directory.
 */
fun Path.unzipTo(targetDirectory: Path) {
    val zipFileSystem = FileSystem.SYSTEM.openZip(this.toString().toPath())
    val fileSystem = FileSystem.SYSTEM
    val paths =
        zipFileSystem
            .listRecursively("/".toPath())
            .filter { zipFileSystem.metadata(it).isRegularFile }
            .toList()

    paths.forEach { zipFilePath ->
        zipFileSystem.source(zipFilePath).buffer().use { source ->
            val relativeFilePath = zipFilePath.toString().trimStart('/')
            val fileToWrite = targetDirectory.toString().toPath().resolve(relativeFilePath)
            fileToWrite.createParentDirectories()
            fileSystem.sink(fileToWrite).buffer().use { sink ->
                sink.writeAll(source)
            }
        }
    }
}

private fun okio.Path.createParentDirectories() {
    this.parent?.let { parent ->
        FileSystem.SYSTEM.createDirectories(parent)
    }
}
