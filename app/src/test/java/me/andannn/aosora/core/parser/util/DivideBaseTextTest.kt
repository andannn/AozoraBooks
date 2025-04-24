package me.andannn.aosora.core.parser.util

import me.andannn.aosora.core.common.model.AozoraElement
import kotlin.test.Test

class DivideBaseTextTest {
    @Test
    fun testDivide() {
        val element = AozoraElement.Text("abcde")
        val (left, right) = element.divide(2)!!
        assert(left.text == "ab")
        assert(right.text == "cde")
    }
}