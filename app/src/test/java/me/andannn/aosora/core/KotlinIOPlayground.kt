package me.andannn.aosora.core

import kotlinx.io.Buffer
import kotlinx.io.indexOf
import kotlinx.io.readByteArray
import kotlinx.io.readCodePointValue
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlin.test.Test

class KotlinIOPlayground {

    @Test
    fun testKotlinIO() {
        val sample = "傍に畏る葬式彦と緒《とも》に、いささか出鼻を挫《くじ》かれた心持ちで、に［＃「に」に傍点］組の頭常吉の言葉に先刻から耳を傾けている。"

        val buffer = Buffer()
        buffer.writeString(sample)
//        while (!buffer.exhausted()) {
//            println(buffer.readByte().toInt().and(0xFF).toString(2).padStart(8, '0'))
//        }

        buffer.skip(2)
        val (str, count)= buffer.readUtf8Block(2)
        println(str)
        println(count)
    }

    fun Buffer.readUtf8Block(maxBytes: Int): Pair<String, Int> {
        val builder = StringBuilder()
        var readBytes = 0
        while (readBytes < maxBytes && !exhausted()) {
            val codePoint = readCodePointValue()
            if (REPLACEMENT_CODE_POINT == codePoint) {
                readBytes += 1
                // skip replacement character
                continue
            }
            builder.appendCodePoint(codePoint)
            readBytes += codePoint.utf8Size()
        }
        return builder.toString() to readBytes
    }

    fun Int.utf8Size(): Int = when (this) {
        in 0x0000..0x007F -> 1  // ASCII
        in 0x0080..0x07FF -> 2  // 2-byte UTF-8
        in 0x0800..0xFFFF -> 3  // Most CJK and symbols
        in 0x10000..0x10FFFF -> 4 // Emoji, rare symbols
        else -> 1 // fallback for malformed
    }
}

internal const val REPLACEMENT_CHARACTER: Char = '\ufffd'
internal const val REPLACEMENT_CODE_POINT: Int = REPLACEMENT_CHARACTER.code

