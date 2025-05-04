package me.andannn.aozora.core.parser

import kotlinx.io.InternalIoApi
import kotlinx.io.Source
import kotlinx.io.indexOf
import kotlinx.io.readString

/**
 * @property index start index of this line
 * @property length read byte count
 * @property content line content
 */
data class RawLine constructor(
    val index: Long,
    val length: Long,
    val content: String,
)

/**
 * create line sequence from source.
 */
@OptIn(InternalIoApi::class)
fun Source.lineSequence() =
    sequence<RawLine> {
        var currentIndex = 0L
        while (!exhausted()) {
            var lfIndex = indexOf('\n'.code.toByte())
            when (lfIndex) {
                -1L -> {
                    yield(
                        RawLine(
                            index = currentIndex,
                            length = buffer.size,
                            content = readString(),
                        ),
                    )
                    currentIndex += buffer.size
                } // buffer .size
                0L -> {
                    skip(1) // empty readCount 1
                    yield(
                        RawLine(
                            index = currentIndex,
                            length = 1,
                            content = "",
                        ),
                    )
                    currentIndex += 1
                }

                else -> {
                    var skipBytes = 1
                    if (buffer[lfIndex - 1] == '\r'.code.toByte()) {
                        lfIndex -= 1
                        skipBytes += 1
                    }
                    val string = readString(lfIndex)
                    skip(skipBytes.toLong()) // read lfIndex + skipBytes

                    val consumed = lfIndex + skipBytes
                    yield(
                        RawLine(
                            index = currentIndex,
                            length = consumed,
                            content = string,
                        ),
                    )
                    currentIndex += consumed
                }
            }
        }
    }
