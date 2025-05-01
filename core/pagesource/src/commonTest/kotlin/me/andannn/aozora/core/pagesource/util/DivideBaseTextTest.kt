package me.andannn.aozora.core.pagesource.util

import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.BlockType
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
            AozoraBlock(
                elements =
                    listOf(
                        AozoraElement.Text("ab"),
                        AozoraElement.Text("cdef"),
                        AozoraElement.Text("ghijk"),
                    ),
                blockType = BlockType.Text(),
                byteRange = 0L..10L,
            )
        val (left, right) = block.divideByTextIndex(3)
        assertEquals(AozoraElement.Text("c"), left.elements[1])
        assertEquals(AozoraElement.Text("def"), right.elements[0])
    }

    @Test
    fun testDiveBlockWithRuby() {
        val block =
            AozoraBlock(
                elements =
                    listOf(
                        AozoraElement.Text("ab"),
                        AozoraElement.Ruby("cdef", ruby = "AA"),
                        AozoraElement.Text("ghijk"),
                    ),
                blockType = BlockType.Text(),
                byteRange = 0L..0L,
            )
        val (left, right) = block.divideByTextIndex(3)
        assertIs<AozoraElement.Ruby>(left.elements[1])
    }
}
