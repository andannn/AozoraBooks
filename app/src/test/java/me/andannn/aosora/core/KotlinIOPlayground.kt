package me.andannn.aosora.core

import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.indexOf
import kotlinx.io.readLine
import kotlinx.io.readString
import kotlinx.io.writeString
import me.andannn.aosora.core.common.util.readBytesBeforeBreakLineTo
import me.andannn.aosora.core.common.util.readUtf8ContinuationBytesTo
import kotlin.math.min
import kotlin.test.Test

class KotlinIOPlayground {

    @Test
    fun testKotlinIO() {
//        val cachedBuffer = buffer.copy()
//
//        val bufferBefore = Buffer()
//        var current = 0
//        buffer.readTo(bufferBefore, 8)
//        current += 8
//        current += buffer.readUtf8ContinuationBytesTo(bufferBefore)
//        println(bufferBefore.peek().readString())
//        println(bufferBefore.peek().readString())
//        println(bufferBefore.readString())

        val sample = buildString {
            repeat(10) {
                append("あ")
            }

            append('\n')
            repeat(10) {
                append("い")
            }
            append('\n')

            repeat(10) {
                append("う")
            }
        }
        var buffer = Buffer()
        buffer.writeString(sample)

        val chuckSize = 44L
        sequence<String> {
            while (!buffer.exhausted()) {
                val remainedBuffer = Buffer()
                if (buffer.size > chuckSize) {
                    buffer.readAtMostTo(remainedBuffer, chuckSize)
                }

                // write back the remained buffer
                buffer.readUtf8ContinuationBytesTo(remainedBuffer)

                // write half of line to remain buffer
//                buffer.readBytesBeforeBreakLineTo(remainedBuffer)

                // consume buffer
                consumeBufferEfficient(buffer)

                buffer.close()
                buffer = remainedBuffer
            }
        }.forEach {
            println(it)
        }

//        val remainBuffer = Buffer()
//        buffer.readTo(remainBuffer, 200)
//        println(buffer.readString())
//        println()
//
//        val buffer2 = Buffer()
//        remainBuffer.readTo(buffer2, 100)
//
//        println(remainBuffer.readString())
//        println()
//        println(buffer2.readString())
//        println()

//        println()
//        println()
//        println()
//        println(cachedBuffer.readString())
    }

    /**
     * Consume the [buffer] line by line, yielding each line.
     *
     * @return the total number of bytes consumed from the buffer.
     */
    suspend fun SequenceScope<String>.consumeBufferEfficient(buffer: Buffer): Int {
        var totalBytesConsumed = 0

        while (!buffer.exhausted()) {
            // 查找下一次换行符
            val newlineIndex = buffer.indexOf('\n'.code.toByte())

            if (newlineIndex == -1L) {
                // 没有换行符了，直接读剩下的全部
                val byteCount = buffer.size
                val text = buffer.readLine()
                if (text != null) {
                    yield(text)
                }
                totalBytesConsumed += byteCount.toInt()
                break
            } else {
                // 找到了换行符，读到这个地方
                val line = buffer.readString(newlineIndex)
                buffer.skip(1) // skip '\n'
                yield(line)
                totalBytesConsumed += (newlineIndex + 1).toInt()
            }
        }

        return totalBytesConsumed
    }
}
