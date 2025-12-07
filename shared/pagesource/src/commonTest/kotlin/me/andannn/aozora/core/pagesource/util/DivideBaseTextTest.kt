/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.util

import me.andannn.aozora.core.domain.model.AozoraElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class DivideBaseTextTest {
    @Test
    fun testDivideRuby() {
        val element = AozoraElement.Ruby("1234", ruby = "AA")
        element.divide(1).also { (left, right) ->
            assertEquals(AozoraElement.NoDividedLeftText, left)
            assertEquals("1234", right.text)
        }
    }

    @Test
    fun testDivide2() {
        val element = AozoraElement.Text("1234")
        assertFails {
            element.divide(0)
        }
        element.divide(1).also { (left, right) ->
            assertEquals("1", left.text)
            assertEquals("234", right.text)
        }
        element.divide(2).also { (left, right) ->
            assertEquals("12", left.text)
            assertEquals("34", right.text)
        }
        element.divide(3).also { (left, right) ->
            assertEquals("123", left.text)
            assertEquals("4", right.text)
        }
        assertFails {
            element.divide(4)
        }
    }
}
