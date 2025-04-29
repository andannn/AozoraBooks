package me.andannn.aozora.ui.rendering.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.ui.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.rendering.ElementRenderAdapter
import me.andannn.aozora.ui.rendering.PaintProvider

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