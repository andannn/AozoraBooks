package me.andannn.core.util

import io.ktor.utils.io.charsets.decode
import io.ktor.utils.io.charsets.forName
import kotlinx.io.Source

actual fun readStringFromSource(
    source: Source,
    charset: String,
): String = Charsets.forName(charset).newDecoder().decode(source)
