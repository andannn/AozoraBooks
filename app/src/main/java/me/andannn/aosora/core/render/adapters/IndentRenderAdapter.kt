package me.andannn.aosora.core.render.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aosora.core.common.model.FontStyle
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.render.ElementRenderAdapter
import me.andannn.aosora.core.render.PaintProvider
import me.andannn.aosora.ui.reader.DEBUG_RENDER

class IndentRenderAdapter(
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
        element as? AozoraElement.Indent ?: return null
        if (fontStyle == null) {
            error("fontStyle must not be null")
        }

        val paint = paintProvider.getPaint(fontStyle, textColor = textColor)

        if (DEBUG_RENDER) {
            canvas.drawLine(
                x,
                y,
                x,
                y + paint.textSize * element.count,
                paintProvider.getDebugPaint()
            )
        }
        return Size(0f, paint.textSize * element.count)
    }
}