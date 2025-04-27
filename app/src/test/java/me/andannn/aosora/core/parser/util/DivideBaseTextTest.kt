package me.andannn.aosora.core.parser.util

import me.andannn.aosora.core.common.model.AozoraBlock
import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.common.model.AozoraTextStyle
import me.andannn.aosora.core.common.model.BlockType
import me.andannn.aosora.core.pagesource.util.divide
import me.andannn.aosora.core.pagesource.util.divideByTextIndex
import kotlin.test.Test

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