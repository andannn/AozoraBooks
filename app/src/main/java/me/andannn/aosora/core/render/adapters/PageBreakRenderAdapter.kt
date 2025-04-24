package me.andannn.aosora.core.render.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aosora.core.common.model.FontStyle
import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.render.ElementRenderAdapter
import me.andannn.aosora.core.render.PaintProvider
import me.andannn.aosora.ui.reader.DEBUG_RENDER

class PageBreakRenderAdapter(
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
        element as? AozoraElement.PageBreak ?: return null
        if (DEBUG_RENDER) {
            canvas.drawLine(
                x-20f,
                y,
                x + 20f,
                y + 40,
                paintProvider.getDebugPaint()
            )
        }
        return Size(0f, 0f)
    }
}