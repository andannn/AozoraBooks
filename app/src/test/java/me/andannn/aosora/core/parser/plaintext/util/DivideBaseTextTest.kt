package me.andannn.aosora.core.parser.plaintext.util

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.util.divide
import org.junit.Test

class DivideBaseTextTest {
    @Test
    fun testDivide() {
        val element = AozoraElement.Text("abcde")
        val (left, right) = element.divide(2)!!
        assert(left.text == "ab")
        assert(right.text == "cde")
    }
}