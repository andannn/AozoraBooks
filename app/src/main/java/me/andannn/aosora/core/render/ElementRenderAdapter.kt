package me.andannn.aosora.core.render

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aosora.core.common.FontStyle
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.render.adapters.BasicTextRenderAdapter

interface ElementRenderAdapter {

    /**
     * Draws the given element at the given coordinates.
     * @param x The x coordinate to draw the element at.
     * @param y The y coordinate to draw the element at.
     * @param element The element to draw.
     * @param fontStyle The font style to use when drawing the element.
     *
     * @return The size of the drawn element.
     */
    fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle? = null
    ): Size

    companion object {
        val DefaultAdapters = createAdapters()
    }
}

fun createAdapters(paintProvider: PaintProvider = DefaultPaintProvider()): List<ElementRenderAdapter> {
    return listOf(
        BasicTextRenderAdapter(paintProvider)
    )
}