package me.andannn.aozora.ui.feature.reader.viewer

import android.graphics.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.data.common.AozoraPage.AozoraLayoutPage
import me.andannn.aozora.core.data.common.Line
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.rendering.DefaultPaintProvider
import me.andannn.aozora.ui.feature.reader.viewer.rendering.ElementRenderAdapter
import me.andannn.aozora.ui.feature.reader.viewer.rendering.ElementRenderAdapter.Companion.DefaultAdapters

private const val TAG = "PageView"

@Composable
fun PageView(
    modifier: Modifier = Modifier,
    page: AozoraLayoutPage,
    textColor: Int,
) {
    Napier.d(tag = TAG) { "PageView E. page ${page.hashCode()}" }
    val adapters =
        remember {
            DefaultAdapters
        }
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .drawWithContent {
                    drawIntoCanvas { canvas ->
                        val renderWidth = page.metaData.renderWidth
                        val renderHeight = page.metaData.renderHeight
                        val offsetX = page.metaData.offset.first
                        val offsetY = page.metaData.offset.second

                        if (DEBUG_RENDER) {
                            canvas.nativeCanvas.drawRect(
                                offsetX,
                                offsetY,
                                offsetX + renderWidth,
                                offsetY + renderHeight,
                                DefaultPaintProvider().getDebugPaint(),
                            )
                        }
                        canvas.save()
                        canvas.nativeCanvas.translate(offsetX, offsetY)

                        var currentX = renderWidth
                        for (line in page.lines) {
                            currentX -= line.lineHeight / 2
                            canvas.nativeCanvas.drawAozoraLine(currentX, line, adapters, textColor)
                            currentX -= line.lineHeight / 2
                        }

                        canvas.restore()
                    }
                    drawContent()
                },
    )
}

fun Canvas.drawAozoraLine(
    x: Float,
    line: Line,
    adapters: List<ElementRenderAdapter>,
    textColor: Int,
) {
    val fontStyle = line.fontStyle
    var currentY = 0f
    line.elements.forEach { element ->
        var drawSize: Size? = null
        for (adapter in adapters) {
            val size = adapter.draw(this, x, currentY, element, fontStyle, textColor)
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
                x - line.lineHeight / 2,
                currentY,
                x + line.lineHeight / 2,
                currentY + drawSize.height,
                DefaultPaintProvider().getDebugPaint(),
            )
        }

        currentY += drawSize.height
    }
}
