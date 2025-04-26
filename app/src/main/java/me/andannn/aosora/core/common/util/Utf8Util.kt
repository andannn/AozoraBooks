package me.andannn.aosora.core.common.util

import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.indexOf
import kotlinx.io.readCodePointValue
import kotlinx.io.readLine


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
 * Reads a UTF-8 string from the buffer.
 * Ensures that at least [byteThreshold] bytes are consumed (may be more to avoid cutting off multi-byte characters).
 *
 * @return a pair of (string, actual bytes consumed).
 * @throws IllegalArgumentException if the buffer starts with an invalid UTF-8 sequence.
 */
fun Buffer.readUtf8StringByThreshold(byteThreshold: Int): Pair<String, Int> {
    require(byteThreshold > 0) { "byteThreshold must be greater than zero." }

    val builder = StringBuilder()
    var readBytes = 0
    while (readBytes < byteThreshold && !exhausted()) {
        val codePoint = readCodePointValue()
        if (REPLACEMENT_CODE_POINT == codePoint) {
            error("Invalid UTF-8 sequence.")
        }
        builder.appendCodePoint(codePoint)
        readBytes += codePoint.utf8Size()
    }
    return builder.toString() to readBytes
}

private fun Int.utf8Size(): Int = when (this) {
    in 0x0000..0x007F -> 1  // ASCII
    in 0x0080..0x07FF -> 2  // 2-byte UTF-8
    in 0x0800..0xFFFF -> 3  // Most CJK and symbols
    in 0x10000..0x10FFFF -> 4 // Emoji, rare symbols
    else -> 1 // fallback for malformed
}

private const val REPLACEMENT_CHARACTER: Char = '\ufffd'
private const val REPLACEMENT_CODE_POINT: Int = REPLACEMENT_CHARACTER.code
