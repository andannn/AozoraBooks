package me.andannn.aosora.core.render.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aosora.core.common.FontStyle
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.render.PaintProvider

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