package me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.ui.feature.reader.viewer.rendering.PaintProvider
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DEBUG_RENDER

class RubyRenderAdapter(
    private val paintProvider: PaintProvider
) : BasicTextRenderAdapter(paintProvider) {
    override fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
        textColor: Int,
    ): Size? {
        element as? AozoraElement.Ruby ?: return null
        if (fontStyle == null) {
            error("Ruby element must have a font style")
        }

        val basePaint = paintProvider.getPaint(fontStyle, textColor = textColor)
        val notionPaint =
            paintProvider.getPaint(fontStyle, isNotation = true, textColor = textColor)

        if (element.ruby.isNotEmpty()) {
            val baseHeight = basePaint.measureText(element.text)
            val baseWidth = basePaint.textSize
            val notionHeight = notionPaint.measureText(element.ruby)
            val notionWidth = notionPaint.textSize

            val offsetHeight = (baseHeight - notionHeight) / 2

            canvas.drawText(
                element.ruby,
                x + baseWidth / 2 + notionWidth / 2,
                y + offsetHeight,
                notionPaint,
            )

            if (DEBUG_RENDER) {
                canvas.drawRect(
                    x + baseWidth / 2,
                    y + offsetHeight,
                    x + baseWidth / 2 + notionWidth,
                    y + offsetHeight + notionHeight,
                    paintProvider.getDebugPaint()
                )
            }
        }

        return super.draw(canvas, x, y, element, fontStyle, textColor)
    }
}