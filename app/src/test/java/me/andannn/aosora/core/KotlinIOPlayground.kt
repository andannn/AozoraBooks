package me.andannn.aosora.core

import kotlinx.io.Buffer
import kotlinx.io.writeString
import me.andannn.aosora.core.common.util.readUtf8ContinuationBytesTo
import kotlin.test.Test

class KotlinIOPlayground {

    @Test
    fun testKotlinIO() {
        val sample =
            "傍に畏る葬式彦と緒《とも》に、いささか出鼻を挫《くじ》かれた心持ちで、に［＃「に」に傍点］組の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。の頭常吉の言葉に先刻から耳を傾けている。"

        val buffer = Buffer()
        buffer.writeString(sample)
        val bufferBefore = Buffer()
        var current = 0
        buffer.readTo(bufferBefore, 8)
        current += 8
        current += buffer.readUtf8ContinuationBytesTo(bufferBefore)
    }
}
