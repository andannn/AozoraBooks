/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.util

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import me.andannn.core.util.readString
import me.andannn.core.util.unzip
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

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
    fun readStringTest() {
        val str =
            Path("src/commonTest/resources/test3.html").readString("Shift_JIS")
        println(str)
    }

    @Test
    fun unzipFileTest() {
        Path("src/commonTest/resources/test.zip").unzip(testPath)
    }
}
