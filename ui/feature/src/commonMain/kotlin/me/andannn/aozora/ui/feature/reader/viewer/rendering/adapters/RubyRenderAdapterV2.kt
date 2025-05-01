package me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.ui.common.theme.RandomColor
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.rendering.MeasureHelper

class RubyRenderAdapterV2(
    private val measureHelper: MeasureHelper,
) : BasicTextRenderAdapterV2(measureHelper) {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
    ): Size? {
        element as? AozoraElement.Ruby ?: return null
        if (fontStyle == null) {
            error("Ruby element must have a font style")
        }

        if (element.ruby.isNotEmpty()) {
            val baseSize =
                measureHelper
                    .measure(
                        text = "あ",
                        fontStyle = fontStyle,
                    ).size
            val baseHeight = baseSize.width.times(element.text.length)
            val baseWidth = baseSize.width

            val notionSize =
                measureHelper
                    .measure(
                        text = "あ",
                        fontStyle = fontStyle,
                        isNotation = true,
                    ).size
            val notionHeight = notionSize.width.times(element.ruby.length).toFloat()
            val notionWidth = notionSize.width.toFloat()

            val offsetHeight = (baseHeight - notionHeight) / 2

            drawVerticalString(
                text = element.ruby,
                fontStyle = fontStyle,
                x + baseWidth / 2 + notionWidth / 2,
                y + offsetHeight,
                isNotation = true,
            )
            if (DEBUG_RENDER) {
                drawRect(
                    topLeft =
                        Offset(
                            x + baseWidth / 2,
                            y + offsetHeight,
                        ),
                    size =
                        Size(
                            notionWidth,
                            notionHeight,
                        ),
                    color = RandomColor,
                )
            }
        }
        return super.drawWithScope(scope = this, x, y, element, fontStyle)
    }
}
