/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.util

import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import kotlinx.io.readString
import kotlinx.io.writeString
import me.andannn.core.util.readStringFromSource
import me.andannn.core.util.unzipTo
import me.andannn.core.util.writeToPath
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Utf8UtilTest {
    @Test
    fun readUtf8StringTest() {
        val textSource = Buffer().apply { writeString("Aéあ缝") }
        val str = readStringFromSource(textSource, "UTF-8")
        assertEquals("Aéあ缝", str)
    }

    @Test
    fun readShiftJISStringTest() {
        val shiftJisBytes =
            byteArrayOf(
                0x82.toByte(),
                0xB1.toByte(),
                0x82.toByte(),
                0xF1.toByte(),
                0x82.toByte(),
                0xC9.toByte(),
                0x82.toByte(),
                0xBF.toByte(),
                0x82.toByte(),
                0xCD.toByte(),
            )
        val textSource = Buffer().apply { write(shiftJisBytes) }
        val str = readStringFromSource(textSource, "Shift_JIS")
        assertEquals("こんにちは", str)
    }

    @Test
    @Ignore
    fun unzipFileTest() {
        val testPath = Path(SystemTemporaryDirectory, "test.zip")
        Path("src/commonTest/resources/test.zip").unzipTo(testPath)
        assertEquals(
            1,
            SystemFileSystem.list(testPath).count { it.toString().endsWith("kiso_dochuki.txt") },
        )
    }

    @Test
    fun writeSourceToPathTest() {
        val source = Buffer().apply { writeString("Test") }
        source.writeToPath(Path(SystemTemporaryDirectory, "target.zip"))
        val string =
            SystemFileSystem
                .source(Path(SystemTemporaryDirectory, "target.zip"))
                .buffered()
                .readString()

        assertTrue(SystemFileSystem.exists(Path(SystemTemporaryDirectory, "target.zip")))
        assertEquals(string, "Test")
    }
}
