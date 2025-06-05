/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.service.impl

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.io.files.Path
import me.andannn.core.util.readString
import kotlin.test.Test
import kotlin.test.assertEquals

class AozoraServiceImplTest {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    @Test
    fun parseBookColumnTest() {
        val result =
            parseBookListFromOpeningBooks(
                Path("src/commonTest/resources/test.html").readString("Utf-8"),
            )
        println(result)
    }

    @Test
    fun parsePageCountTest() {
        val result =
            parsePageCount(
                Path("src/commonTest/resources/test.html").readString("Utf-8"),
            )
        assertEquals(10, result)
    }
}
