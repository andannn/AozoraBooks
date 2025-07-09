/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.raw

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test

class RemoteOrCacheBookRawSourceTest {
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
    @Ignore
    fun processParseHtmlTest() {
        processParseHtml(
            Path("src/commonTest/resources/test2.html"),
            testPath,
        )
    }
}
