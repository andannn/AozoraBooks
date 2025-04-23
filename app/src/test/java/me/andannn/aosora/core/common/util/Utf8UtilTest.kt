package me.andannn.aosora.core.common.util

import kotlinx.io.Buffer
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

        val writeBuffer = Buffer()
        val readSize = buffer.readUtf8ContinuationBytesTo(writeBuffer)
        assert(readSize == 2)
        assertEquals(byteArray[1], writeBuffer.readByte())
        assertEquals(byteArray[2], writeBuffer.readByte())
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