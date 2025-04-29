package me.andannn.aozora.ui.rendering.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.ui.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.rendering.ElementRenderAdapter
import me.andannn.aozora.ui.rendering.PaintProvider

abstract class BasicTextRenderAdapter(
    private val paintProvider: PaintProvider
): ElementRenderAdapter {
    override fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
        textColor: Int
    ): Size? {
        element as? AozoraElement.BaseText ?: return null
        if (fontStyle == null) {
            error("fontStyle must not be null $element")
        }

        val paint = paintProvider.getPaint(fontStyle, textColor = textColor)
        val height = paint.measureText(element.text)
        val width = paint.textSize
        canvas.drawText(
            /* text = */ element.text,
            /* start = */ 0,
            /* end = */ element.text.length,
            /* x = */ x,
            /* y = */ y,
            /* paint = */ paint
        )

        if (DEBUG_RENDER) {
            canvas.drawRect(
                x - width / 2,
                y,
                x + width / 2,
                y + height,
                paintProvider.getDebugPaint()
            )
        }

        return Size(width, height)
    }
}

class TextRenderAdapter(
    paintProvider: PaintProvider
): BasicTextRenderAdapter(paintProvider) {
    override fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
        textColor: Int
    ): Size? {
        element as? AozoraElement.Text ?: return null
        return super.draw(canvas, x, y, element, fontStyle, textColor)
    }
}