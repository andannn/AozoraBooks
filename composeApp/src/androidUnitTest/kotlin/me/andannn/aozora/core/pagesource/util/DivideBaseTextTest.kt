package me.andannn.aozora.core.pagesource.util

import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.BlockType
import org.junit.Test

class DivideBaseTextTest {
    @Test
    fun testDivide() {
        val element = AozoraElement.Text("abcde")
        val (left, right) = element.divide(2)!!
        assert(left.text == "ab")
        assert(right.text == "cde")
    }

    @Test
    fun testDiveBlock() {
        val block = AozoraBlock(
            elements = listOf(
                AozoraElement.Text("ab"),
                AozoraElement.Text("cdef"),
                AozoraElement.Text("ghijk"),
            ),
            blockType = BlockType.Text(),
            byteRange = 0L..10L
        )
        val (left, right ) = block.divideByTextIndex(3)
        assert(left.elements[1] == AozoraElement.Text("c"))
        assert(right.elements[0] == AozoraElement.Text("def"))
    }
    @Test
    fun testDiveBlockWithRuby() {
        val block = AozoraBlock(
            elements = listOf(
                AozoraElement.Text("ab"),
                AozoraElement.Ruby("cdef", ruby = "AA"),
                AozoraElement.Text("ghijk"),
            ),
            blockType = BlockType.Text(),
            byteRange = 0L..0L
        )
        val (left, right ) = block.divideByTextIndex(3)
        assert(left.elements[1] is AozoraElement.Ruby)
    }
}