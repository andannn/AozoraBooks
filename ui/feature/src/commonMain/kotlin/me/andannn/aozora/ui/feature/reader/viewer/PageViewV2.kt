package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.data.common.AozoraPage.AozoraLayoutPage
import me.andannn.aozora.core.data.common.Line
import me.andannn.aozora.ui.common.theme.RandomColor
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.rendering.ElementRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.rendering.createAdapters

private const val TAG = "PageView"

@Composable
fun PageViewV2(
    modifier: Modifier = Modifier,
    page: AozoraLayoutPage,
    textColor: Color,
) {
    Napier.d(tag = TAG) { "PageView E. page ${page.hashCode()}" }
    val measurer = rememberTextMeasurer(
        cacheSize = 0
    )
    val density = LocalDensity.current
    val adapters = remember(measurer, density) {
        createAdapters(measurer, density)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                val renderWidth = page.metaData.renderWidth
                val renderHeight = page.metaData.renderHeight
                val offsetX = page.metaData.offset.first
                val offsetY = page.metaData.offset.second

                if (DEBUG_RENDER) {
                    drawRect(
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(renderWidth, renderHeight),
                        color = RandomColor
                    )
                }
                translate(
                    offsetX, offsetY
                ) {
                    var currentX = renderWidth
                    for (line in page.lines) {
                        currentX -= line.lineHeight / 2
                        drawAozoraLineV2(currentX, line, textColor, adapters)
                        currentX -= line.lineHeight / 2
                    }
                }
            }
    )
}

fun DrawScope.drawAozoraLineV2(
    x: Float,
    line: Line,
    textColor: Color,
    adapters: List<ElementRenderAdapterV2>,
) {
    val fontStyle = line.fontStyle
    var currentY = 0f
    line.elements.forEach { element ->
        var drawSize: Size? = null
        for (adapter in adapters) {
            val size = with(adapter) { draw(x, currentY, element, fontStyle, textColor) }
            if (size != null) {
                drawSize = size
                break
            }
        }
        if (drawSize == null) {
            error("No adapter can draw element $element")
        }

        if (DEBUG_RENDER) {
            drawRect(
                topLeft = Offset(
                    x - line.lineHeight / 2,
                    currentY,
                ),
                size = Size(
                    line.lineHeight,
                    drawSize.height
                ),
                color = RandomColor
            )
        }

        currentY += drawSize.height
    }
}

