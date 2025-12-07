/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class NDCClassificationTest {
    @Test
    fun testNDCClassification() {
        val ndc1 = NDCClassification("1")
        assertEquals(ndc1.mainClassNum, 1)
        assertEquals(ndc1.divisionNum, null)
        assertEquals(ndc1.sectionNum, null)
        assertEquals(ndc1.ndcType, NDCType.MAIN_CLASS)

        val ndc2 = NDCClassification("12")
        assertEquals(ndc2.mainClassNum, 1)
        assertEquals(ndc2.divisionNum, 2)
        assertEquals(ndc2.sectionNum, null)
        assertEquals(ndc2.ndcType, NDCType.DIVISION)

        val ndc3 = NDCClassification("123")
        assertEquals(ndc3.mainClassNum, 1)
        assertEquals(ndc3.divisionNum, 2)
        assertEquals(ndc3.sectionNum, 3)
        assertEquals(ndc3.ndcType, NDCType.SECTION)

        assertFails {
            NDCClassification("")
        }

        assertFails {
            NDCClassification("1234")
        }
    }

    @Test
    fun testNDCClassificationInvalid() {
        assertFails { NDCClassification("1a") }
        assertFails { NDCClassification("12b") }
        assertFails { NDCClassification("123c") }
        assertFails { NDCClassification("1234d") }
        assertFails { NDCClassification("12 3") }
    }

    @Test
    fun testNDCClassificationIsChildOf() {
        val mainClass = NDCClassification("1")
        val division = NDCClassification("12")
        val section = NDCClassification("123")

        assertTrue(section.isChildOf(division))
        assertTrue(division.isChildOf(mainClass))
        assertTrue(!section.isChildOf(mainClass))
        assertTrue(!mainClass.isChildOf(division))
        assertTrue(!mainClass.isChildOf(section))
        assertTrue(!division.isChildOf(division))
        assertTrue(!section.isChildOf(section))
        assertTrue(!mainClass.isChildOf(mainClass))
    }

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
