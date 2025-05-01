package me.andannn.aozora.ui.feature.reader.viewer.rendering.adapters

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.ui.common.theme.RandomColor
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.rendering.ElementRenderAdapterV2

class PageBreakRenderAdapterV2 : ElementRenderAdapterV2 {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
    ): Size? {
        element as? AozoraElement.PageBreak ?: return null
        if (DEBUG_RENDER) {
            drawLine(
                color = RandomColor,
                start =
                    Offset(
                        x - 20f,
                        y,
                    ),
                end =
                    Offset(
                        x + 20f,
                        y + 40,
                    ),
            )
        }
        return Size(0f, 0f)
    }
}
