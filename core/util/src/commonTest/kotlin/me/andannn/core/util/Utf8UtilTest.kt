/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.core.util

import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.io.Buffer
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Utf8UtilTest {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private val randomIndex: Int = Random.Default.nextInt()

    private lateinit var testPath: Path

    @BeforeTest
    fun setUp() {
        testPath = Path("$SystemTemporaryDirectory/test_download_$randomIndex")
        SystemFileSystem.createDirectories(path = testPath)
        println(testPath)
    }

    @Test
    fun readUtf8ContinuationBytesToTest() {
        val sample = "傍"
        val byteArray = sample.toByteArray() // size 3
        val buffer = Buffer()
        buffer.writeString(sample)

        buffer.skip(1)

        val sink = Buffer()
        val readSize = buffer.readUtf8ContinuationBytesTo(sink)
        assertEquals(2, readSize)
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
    fun readStringTest() {
        val str =
            Path("src/commonTest/resources/test.html").readString("Shift_JIS")
        println(str)
    }

    @Test
    fun unzipFileTest() {
        Path("src/commonTest/resources/test.zip").unzip(testPath)
    }
}
