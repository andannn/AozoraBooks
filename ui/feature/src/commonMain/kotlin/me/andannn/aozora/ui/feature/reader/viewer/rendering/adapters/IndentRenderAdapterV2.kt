package me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.ui.common.theme.RandomColor
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.rendering.ElementRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.rendering.MeasureHelper

class IndentRenderAdapterV2(
    private val measureHelper: MeasureHelper,
) : ElementRenderAdapterV2 {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
    ): Size? {
        element as? AozoraElement.Indent ?: return null
        if (fontStyle == null) {
            error("fontStyle must not be null")
        }

        val result = measureHelper.measure("あ", fontStyle)
        val textSize = result.size.width.toFloat()
        if (DEBUG_RENDER) {
            repeat(element.count) {
                drawCircle(
                    center =
                        Offset(
                            x = x,
                            y = y + textSize / 2 + textSize * it,
                        ),
                    radius = textSize / 2,
                    color = RandomColor,
                )
            }
        }
        return Size(0f, textSize * element.count)
    }
}
