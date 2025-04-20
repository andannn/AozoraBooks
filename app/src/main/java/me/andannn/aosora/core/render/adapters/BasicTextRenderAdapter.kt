package me.andannn.aosora.core.render.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aosora.core.common.FontStyle
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.render.ElementRenderAdapter
import me.andannn.aosora.core.render.PaintProvider

open class BasicTextRenderAdapter(
    private val paintProvider: PaintProvider
): ElementRenderAdapter {
    override fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?
    ): Size {
        element as AozoraElement.BaseText
        if (fontStyle == null) {
            error("fontStyle must not be null")
        }

        val paint = paintProvider.getPaint(fontStyle)
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
        return Size(width, height)
    }
}