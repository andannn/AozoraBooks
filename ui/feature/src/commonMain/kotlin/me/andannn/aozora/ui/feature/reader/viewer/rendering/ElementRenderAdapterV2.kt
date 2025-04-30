package me.andannn.aozora.ui.feature.reader.viewer.rendering

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.EmphasisRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.IndentRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.LineBreakRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.PageBreakRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.RubyRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.TextRenderAdapterV2

interface ElementRenderAdapterV2 {

    /**
     * Draws the given element at the given coordinates.
     * @param x The x coordinate to draw the element at.
     * @param y The y coordinate to draw the element at.
     * @param element The element to draw.
     * @param fontStyle The font style to use when drawing the element.
     *
     * @return The size of the drawn element or null if the element cannot be drawn.
     */
    fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle? = null,
        textColor: Color
    ): Size?
}

fun createAdapters(
    measurer: TextMeasurer,
    density: Density
): List<ElementRenderAdapterV2> {
    val measureHelper = DefaultMeasureHelper(measurer, density)
    return listOf(
        EmphasisRenderAdapterV2(measureHelper),
        IndentRenderAdapterV2(measureHelper),
        LineBreakRenderAdapterV2(measureHelper),
        PageBreakRenderAdapterV2(),
        RubyRenderAdapterV2(measureHelper),
        TextRenderAdapterV2(measureHelper),
    )
}

interface MeasureHelper {
    fun measure(
        text: String,
        fontStyle: FontStyle,
        isNotation: Boolean = false,
    ): TextLayoutResult
}

class DefaultMeasureHelper(
    private val measurer: TextMeasurer,
    private val density: Density
) : MeasureHelper {
    override fun measure(
        text: String,
        fontStyle: FontStyle,
        isNotation: Boolean,
    ): TextLayoutResult {
        val fontSizePx = if (isNotation) fontStyle.notationSize else fontStyle.baseSize
        val fontType = fontStyle.fontType
        val fontSizeSp = with(density) { fontSizePx.toSp() }
        return measurer.measure(
            text,
            TextStyle(
                fontSize = fontSizeSp,
            )
        )
    }
}

