package me.andannn.aozora.ui.feature.reader.viewer.rendering

import android.graphics.Canvas
import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.EmphasisRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.IndentRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.LineBreakRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.PageBreakRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.RubyRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters.TextRenderAdapter

interface ElementRenderAdapter {
    /**
     * Draws the given element at the given coordinates.
     * @param x The x coordinate to draw the element at.
     * @param y The y coordinate to draw the element at.
     * @param element The element to draw.
     * @param fontStyle The font style to use when drawing the element.
     * @param textColor The color of the text to use when drawing the element.
     *
     * @return The size of the drawn element or null if the element cannot be drawn.
     */
    fun draw(
        canvas: Canvas,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle? = null,
        textColor: Int,
    ): Size?

    companion object {
        val DefaultAdapters = createAdapters()
    }
}

fun createAdapters(paintProvider: PaintProvider = DefaultPaintProvider()): List<ElementRenderAdapter> {
    return listOf(
        TextRenderAdapter(paintProvider),
        RubyRenderAdapter(paintProvider),
        LineBreakRenderAdapter(paintProvider),
        EmphasisRenderAdapter(paintProvider),
        IndentRenderAdapter(paintProvider),
        PageBreakRenderAdapter(paintProvider),
    )
}
