/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal.util

import kotlin.test.Test
import kotlin.test.assertEquals

class NdcParserUtilTest {
    @Test
    fun testParseStringAsNDCClassification() {
        val input1 = "NDC 931"
        val input2 = "NDC 931 123"
        val input3 = "NDC K726"
        val input4 = "NDC 369 453 524"

        val result1 = input1.asNDCClassification()
        val result2 = input2.asNDCClassification()
        val result3 = input3.asNDCClassification()
        val result4 = input4.asNDCClassification()

        assertEquals(1, result1.size)
        assertEquals("931", result1[0].value)
        assertEquals(2, result2.size)
        assertEquals("931", result2[0].value)
        assertEquals("123", result2[1].value)
        assertEquals(1, result3.size)
        assertEquals("726", result3[0].value)
        assertEquals(3, result4.size)
    }
}
