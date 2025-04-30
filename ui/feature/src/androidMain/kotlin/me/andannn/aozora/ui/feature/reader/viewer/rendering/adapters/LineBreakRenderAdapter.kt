package me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.ui.feature.reader.viewer.rendering.ElementRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.rendering.PaintProvider
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DEBUG_RENDER

class LineBreakRenderAdapter(
    private val paintProvider: PaintProvider
) : ElementRenderAdapter {
    override fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
        textColor: Int
    ): Size? {
        element as? AozoraElement.LineBreak ?: return null
        if (fontStyle == null) {
            error("fontStyle must not be null $element")
        }

        val paint = paintProvider.getPaint(fontStyle, textColor = textColor)

        if (DEBUG_RENDER) {
            canvas.drawRect(
                x - paint.textSize / 2,
                y,
                x + paint.textSize / 2,
                y + paint.textSize,
                paintProvider.getDebugPaint()
            )
        }
        return Size(paint.textSize, 0f)
    }
}


