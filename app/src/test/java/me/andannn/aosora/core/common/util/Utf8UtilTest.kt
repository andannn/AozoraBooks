package me.andannn.aosora.core.common.util

import kotlinx.io.Buffer
import kotlinx.io.readLine
import kotlinx.io.readString
import kotlinx.io.writeString
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Utf8UtilTest {
    @Test
    fun readUtf8ContinuationBytesToTest() {
        val sample = "傍"
        val byteArray = sample.toByteArray() // size 3
        val buffer = Buffer()
        buffer.writeString(sample)

        buffer.skip(1)

        val sink = Buffer()
        val readSize = buffer.readUtf8ContinuationBytesTo(sink)
        assert(readSize == 2)
        assertEquals(byteArray[1], sink.readByte())
        assertEquals(byteArray[2], sink.readByte())
    }

    @Test
    fun testReadBytesBeforeBreakLineTest() {
        val sample = "あい\nうえおかきくけこ"
        val buffer = Buffer()
        buffer.writeString(sample)

        val remain = Buffer()

        val size = buffer.readBytesBeforeBreakLineTo(remain)

        assertEquals("あい\n", remain.readString())
        assertEquals("うえおかきくけこ", buffer.readString())
        assertEquals(7, size)
    }

    @Test
    fun readUtf8WithBufferStartFromContinuationByteTest() {
        val sample = "傍"
        val buffer = Buffer()
        buffer.writeString(sample)
        buffer.skip(1)
        assertFailsWith(IllegalStateException::class) {
            buffer.readUtf8StringByThreshold(3)
        }
    }

    @Test
    fun readUtf8StringByThresholdTest() {
        val sample = "傍4f124"
        val buffer = Buffer()
        buffer.writeString(sample)
        assertEquals("傍4f124" to 8, buffer.readUtf8StringByThreshold(128))
    }
}