package me.andannn.aosora

import kotlinx.io.Buffer
import kotlinx.io.indexOf
import kotlinx.io.readLine
import kotlinx.io.writeString
import me.andannn.aosora.common.model.AozoraString
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val buffer = Buffer()
        buffer.writeString("吾輩《わがはい》は猫である。名前はまだ無い。")
        val lineString = buffer.readLine()
        if (lineString != null) {
            println(lineString)
            val index = lineString.indexOf('《')
            sequence<AozoraString> {

            }
        }
    }
}