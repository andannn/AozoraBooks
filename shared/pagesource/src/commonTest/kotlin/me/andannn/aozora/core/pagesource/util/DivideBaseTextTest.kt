/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.util

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.textBlockOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs

class DivideBaseTextTest {
    @Test
    fun testDivideRuby() {
        val element = AozoraElement.Ruby("1234", ruby = "AA")
        element.divide2(1).also { (left, right) ->
            assertEquals(AozoraElement.NoDividedLeftText, left)
            assertEquals("1234", right.text)
        }
    }

    @Test
    fun testDivide2() {
        val element = AozoraElement.Text("1234")
        assertFails {
            element.divide2(0)
        }
        element.divide2(1).also { (left, right) ->
            assertEquals("1", left.text)
            assertEquals("234", right.text)
        }
        element.divide2(2).also { (left, right) ->
            assertEquals("12", left.text)
            assertEquals("34", right.text)
        }
        element.divide2(3).also { (left, right) ->
            assertEquals("123", left.text)
            assertEquals("4", right.text)
        }
        assertFails {
            element.divide2(4)
        }
    }

    @Test
    fun testDiveBlock_divide_element() {
        val block = textBlockOf("ab", "cde")
        assertFails {
            block.divideByTextIndex2(0)
        }
        block.divideByTextIndex2(1).also { (left, right) ->
            left.assertText("a")
            right.assertText("b", "cde")
        }
        block.divideByTextIndex2(2).also { (left, right) ->
            left.assertText("ab")
            right.assertText("cde")
        }
        block.divideByTextIndex2(3).also { (left, right) ->
            left.assertText("ab", "c")
            right.assertText("de")
        }
        block.divideByTextIndex2(4).also { (left, right) ->
            left.assertText("ab", "cd")
            right.assertText("e")
        }
        assertFails {
            block.divideByTextIndex2(5)
        }
    }

    @Test
    fun testDiveBlockWithRuby() {
        val block =
            AozoraBlock.TextBlock(
                elements =
                    listOf(
                        AozoraElement.Text("ab"),
                        AozoraElement.Ruby("cde", ruby = "AA"),
                        AozoraElement.Text("fgh"),
                    ),
                textStyle = AozoraTextStyle.PARAGRAPH,
                blockIndex = 0,
            )

        assertFails {
            block.divideByTextIndex2(0)
        }
        block.divideByTextIndex2(1).also { (left, right) ->
            left.assertText("a")
            right.assertText("b", "cde", "fgh")
        }
        block.divideByTextIndex2(2).also { (left, right) ->
            left.assertText("ab")
            right.assertText("cde", "fgh")
        }
        for (i in 3..4) {
            block.divideByTextIndex2(i).also { (left, right) ->
                left.assertText("ab", AozoraElement.NoDividedLeftText.text)
                right.assertText("cde", "fgh")
            }
        }
        block.divideByTextIndex2(5).also { (left, right) ->
            left.assertText("ab", "cde")
            right.assertText("fgh")
        }
        block.divideByTextIndex2(6).also { (left, right) ->
            left.assertText("ab", "cde", "f")
            right.assertText("gh")
        }
    }
}

private fun AozoraBlock.assertText(vararg textElements: String) {
    val expects = textElements.toList()
    this.elements.forEachIndexed { index, element ->
        val text =
            when (element) {
                is AozoraElement.BaseText -> element.text
                is AozoraElement.Illustration -> TODO()
                is AozoraElement.Indent -> TODO()
                AozoraElement.LineBreak -> TODO()
                AozoraElement.PageBreak -> TODO()
            }

        assertEquals(expects[index], text)
    }
}
