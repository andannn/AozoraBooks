import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.internal.util.divide
import kotlin.test.Test

class AozoraElementTest {
    @Test
    fun testDivide() {
        val element = AozoraElement.Text("abcde")
        val (left, right) = element.divide(2)!!
        assert(left.text == "ab")
        assert(right.text == "cde")
    }
}