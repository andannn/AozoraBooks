package me.andannn.core.util

import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.indexOf
import kotlinx.io.writeString

/**
 * read utf8 continuation bytes to sink.
 * Returns the number of bytes read.
 */
fun Buffer.readUtf8ContinuationBytesTo(sink: Sink): Int {
    var count = 0
    while (!exhausted()) {
        require(1)
        val b = this[0]
        if ((b.toInt() and 0b1100_0000) == 0b1000_0000) {
            sink.writeByte(b)
            this.skip(1)
            count++
        } else {
            break
        }
    }
    return count
}

fun Buffer.readBytesBeforeBreakLineTo(sink: Sink): Int {
    if (indexOf('\n'.code.toByte()) == -1L) return 0

    var count = 0
    while (!exhausted()) {
        val b = readByte()

        if (b == '\n'.code.toByte()) {
            sink.writeByte(b)
            count++
            break
        } else {
            sink.writeByte(b)
            count++
        }
    }

    return count
}

/**
 * convert string to source.
 */
fun String.asSource(): Source {
    val buffer = Buffer()
    buffer.writeString(this)
    return buffer
}

/**
 * read file as string from path.
 */
fun Path.readString(charset: String): String {
    val source = SystemFileSystem.source(this).buffered()
    return readStringFromSource(source, charset)
}

expect fun readStringFromSource(
    source: Source,
    charset: String,
): String
