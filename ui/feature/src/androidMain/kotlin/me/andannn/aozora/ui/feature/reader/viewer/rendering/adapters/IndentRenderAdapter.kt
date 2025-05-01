package me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.rendering.ElementRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.rendering.PaintProvider

class IndentRenderAdapter(
    private val paintProvider: PaintProvider,
) : ElementRenderAdapter {
    override fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
        textColor: Int,
    ): Size? {
        element as? AozoraElement.Indent ?: return null
        if (fontStyle == null) {
            error("fontStyle must not be null")
        }

        val paint = paintProvider.getPaint(fontStyle, textColor = textColor)

        if (DEBUG_RENDER) {
            repeat(element.count) {
                canvas.drawCircle(
                    x,
                    y + paint.textSize / 2 + paint.textSize * it,
                    paint.textSize / 2,
                    paintProvider.getDebugPaint(),
                )
            }
        }
        return Size(0f, paint.textSize * element.count)
    }
}
