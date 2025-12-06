package me.andannn.aozora.core.pagesource.measure

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.pagesource.textBlockOf
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockMeasureScopeTest {
    @Test
    fun test_measure_no_break_line() {
        val blockMeasureScope = BlockMeasureScopeImpl(50.dp, dummyTextStyleCalculator(10.dp))

        blockMeasureScope.measure(
            textBlockOf("12")
        ).also {
            assertEquals(1, it.lineCount)
        }
    }

    @Test
    fun test_measure_break_line() {
        val blockMeasureScope = BlockMeasureScopeImpl(50.dp, dummyTextStyleCalculator(10.dp))

        blockMeasureScope.measure(
            textBlockOf("123456")
        ).also {
            println(it)
            assertEquals(2, it.lineCount)
        }
    }

    @Test
    fun test_measure_with_indent() {
        val blockMeasureScope = BlockMeasureScopeImpl(50.dp, dummyTextStyleCalculator(10.dp))

        blockMeasureScope.measure(
            textBlockOf("123", indent = 3)
        ).also {
            assertEquals(2, it.lineCount)
        }
    }
}

private fun dummyTextStyleCalculator(textSize: Dp): TextStyleCalculator =
    object : TextStyleCalculator {
        override fun resolve(aozoraStyle: AozoraTextStyle): FontStyle =
            FontStyle(
                fontType = FontType.DEFAULT,
                baseSizeDp = textSize,
                notationSizeDp = textSize,
                lineHeightMultiplier = 1.0f,
            )
    }
