/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer.page.rendering

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters.EmphasisRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters.IndentRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters.LineBreakRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters.PageBreakRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters.RubyRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters.TextRenderAdapter

internal interface ElementRenderAdapterV2<T : AozoraElement> {
    /**
     * Draws the given element at the given coordinates.
     * @param x The x coordinate to draw the element at.
     * @param y The y coordinate to draw the element at.
     * @param element The element to draw.
     * @param fontStyle The font style to use when drawing the element.
     *
     * @return The size of the drawn element or null if the element cannot be drawn.
     */
    fun DrawScope.draw(
        x: Float,
        y: Float,
        element: T,
        fontStyle: FontStyle? = null,
    ): Size?
}

internal interface RenderAdapterProvider {
    fun <T : AozoraElement> getAdapter(element: T): ElementRenderAdapterV2<T>
}

internal inline fun <reified T : AozoraElement> DrawScope.drawWithAdapter(
    renderAdapterProvider: RenderAdapterProvider,
    x: Float,
    y: Float,
    element: T,
    fontStyle: FontStyle? = null,
): Size? {
    val adapter = renderAdapterProvider.getAdapter(element)
    return with(adapter) {
        draw(x, y, element, fontStyle)
    }
}

internal fun RenderAdapterProvider(
    measurer: TextMeasurer,
    density: Density,
    fontFamily: FontFamily,
    textColor: Color,
): RenderAdapterProvider {
    val measureHelper = DefaultMeasureHelper(measurer, density, fontFamily, textColor)
    val emphasisRenderAdapter = EmphasisRenderAdapter(measureHelper)
    val rubyRenderAdapter = RubyRenderAdapter(measureHelper)
    val textRenderAdapter = TextRenderAdapter(measureHelper)
    val indentRenderAdapter = IndentRenderAdapter(measureHelper)
    val lineBreakRenderAdapter = LineBreakRenderAdapter(measureHelper)
    val pageBreakRenderAdapter = PageBreakRenderAdapter()

    return object : RenderAdapterProvider {
        @Suppress("UNCHECKED_CAST")
        override fun <T : AozoraElement> getAdapter(element: T): ElementRenderAdapterV2<T> =
            when (element) {
                is AozoraElement.Emphasis -> emphasisRenderAdapter
                is AozoraElement.Ruby -> rubyRenderAdapter
                is AozoraElement.Text -> textRenderAdapter
                is AozoraElement.Indent -> indentRenderAdapter
                AozoraElement.LineBreak -> lineBreakRenderAdapter
                AozoraElement.PageBreak -> pageBreakRenderAdapter
                is AozoraElement.Illustration -> error("not support")
            } as ElementRenderAdapterV2<T>
    }
}

interface MeasureHelper {
    fun measure(
        text: String,
        fontStyle: FontStyle,
        isNotation: Boolean = false,
    ): TextLayoutResult
}

internal class DefaultMeasureHelper(
    private val measurer: TextMeasurer,
    private val density: Density,
    private val fontFamily: FontFamily,
    private val textColor: Color,
) : MeasureHelper {
    override fun measure(
        text: String,
        fontStyle: FontStyle,
        isNotation: Boolean,
    ): TextLayoutResult {
        val fontSizeDp = if (isNotation) fontStyle.notationSizeDp else fontStyle.baseSizeDp
        val fontSizeSp = with(density) { fontSizeDp.toSp() }
        return measurer.measure(
            text,
            TextStyle(
                fontSize = fontSizeSp,
                fontFamily = fontFamily,
                color = textColor,
            ),
        )
    }
}
