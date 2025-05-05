package me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.ui.common.theme.RandomColor
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.ElementRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.MeasureHelper

class LineBreakRenderAdapterV2(
    private val measureHelper: MeasureHelper,
) : ElementRenderAdapterV2 {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
    ): Size? {
        element as? AozoraElement.LineBreak ?: return null
        if (fontStyle == null) {
            error("fontStyle must not be null $element")
        }

        val textSize =
            measureHelper
                .measure(
                    text = "あ",
                    fontStyle = fontStyle,
                ).size.width
                .toFloat()

        if (DEBUG_RENDER) {
            drawRect(
                topLeft = Offset(x - textSize / 2, y),
                size = Size(textSize, textSize),
                color = RandomColor,
            )
        }

        return Size(textSize, 0f)
    }
}
