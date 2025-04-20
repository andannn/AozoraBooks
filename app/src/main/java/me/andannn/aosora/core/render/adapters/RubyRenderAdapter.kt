package me.andannn.aosora.core.render.adapters

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aosora.core.common.FontStyle
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.render.PaintProvider

class RubyRenderAdapter(
    private val paintProvider: PaintProvider
) : BasicTextRenderAdapter(paintProvider) {
    override fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?
    ): Size {
        element as AozoraElement.Ruby
        val drawSize = super.draw(canvas, x, y, element,)

        return drawSize
    }
}