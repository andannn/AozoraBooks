package me.andannn.aosora.core.common.util

import kotlinx.io.files.Path
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

/**
 * Unzip a zip file to a target directory.
 */
fun Path.unzip(targetDirectory: Path) {
    val buffer = ByteArray(1024)

    val zipInputStream = ZipInputStream(FileInputStream(this.toString()))
    var entry = zipInputStream.nextEntry

    while (entry != null) {
        val newFile = File(targetDirectory.toString(), entry.name)

        if (entry.isDirectory) {
            newFile.mkdirs()
        } else {
            newFile.parentFile?.mkdirs()
            FileOutputStream(newFile).use { output ->
                var len: Int
                while (zipInputStream.read(buffer).also { len = it } > 0) {
                    output.write(buffer, 0, len)
                }
            }
        }
        zipInputStream.closeEntry()
        entry = zipInputStream.nextEntry
    }

    zipInputStream.close()
}
