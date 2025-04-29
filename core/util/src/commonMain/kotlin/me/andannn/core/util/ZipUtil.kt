package me.andannn.core.util

import kotlinx.io.files.Path
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer
import okio.openZip
import okio.use

/**
 * Unzip a zip file to a target directory.
 */
fun Path.unzip(targetDirectory: Path) {
    val zipFileSystem = FileSystem.SYSTEM.openZip(this.toString().toPath())
    val fileSystem = FileSystem.SYSTEM
    val paths = zipFileSystem.listRecursively("/".toPath())
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
