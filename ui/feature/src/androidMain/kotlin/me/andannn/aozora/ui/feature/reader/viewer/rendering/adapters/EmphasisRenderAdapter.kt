package me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.ui.feature.reader.viewer.rendering.PaintProvider

class EmphasisRenderAdapter(
    paintProvider: PaintProvider
): BasicTextRenderAdapter(paintProvider)  {
    override fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
        textColor: Int
    ): Size? {
        element as? AozoraElement.Emphasis ?: return null
        if (fontStyle == null) {
            error("fontStyle must not be null $element")
        }
        return super.draw(canvas, x, y, element, fontStyle, textColor)
    }
}