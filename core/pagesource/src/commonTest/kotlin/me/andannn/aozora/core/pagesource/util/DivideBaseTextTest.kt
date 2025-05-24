/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.util

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DivideBaseTextTest {
    @Test
    fun testDivide() {
        val element = AozoraElement.Text("abcde")
        val (left, right) = element.divide(2)!!
        assertEquals("ab", left.text)
        assertEquals("cde", right.text)
    }

    @Test
    fun testDiveBlock() {
        val block =
            AozoraBlock.TextBlock(
                blockIndex = 0,
                textStyle = AozoraTextStyle.PARAGRAPH,
                elements =
                    listOf(
                        AozoraElement.Text("ab"),
                        AozoraElement.Text("cdef"),
                        AozoraElement.Text("ghijk"),
                    ),
            )
        val (left, right) = block.divideByTextIndex(3)
        assertEquals(AozoraElement.Text("c"), left.elements[1])
        assertEquals(AozoraElement.Text("def"), right.elements[0])
    }

    @Test
    fun testDiveBlockWithRuby() {
        val block =
            AozoraBlock.TextBlock(
                elements =
                    listOf(
                        AozoraElement.Text("ab"),
                        AozoraElement.Ruby("cdef", ruby = "AA"),
                        AozoraElement.Text("ghijk"),
                    ),
                textStyle = AozoraTextStyle.PARAGRAPH,
                blockIndex = 0,
            )
        val (left, right) = block.divideByTextIndex(3)
        assertIs<AozoraElement.Ruby>(left.elements[1])
    }
}
