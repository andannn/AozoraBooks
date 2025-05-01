package me.andannn.core.util

import io.ktor.utils.io.charsets.forName
import kotlinx.io.files.Path

actual fun Path.readString(charset: String): String {
    return readStringOfPath(path = this, charset = Charsets.forName(charset))
}
