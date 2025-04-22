package me.andannn.aosora.ui.reader

import android.graphics.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aosora.core.common.FontStyle
import me.andannn.aosora.core.common.FontType
import me.andannn.aosora.core.pager.AozoraPage
import me.andannn.aosora.core.pager.ReaderLine
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.render.DefaultPaintProvider
import me.andannn.aosora.core.render.ElementRenderAdapter
import me.andannn.aosora.core.render.ElementRenderAdapter.Companion.DefaultAdapters

private const val TAG = "PageView"

const val DEBUG_RENDER = false

@Composable
fun PageView(
    modifier: Modifier = Modifier,
    page: AozoraPage,
    textColor: Int,
) {
    Napier.d(tag = TAG) { "PageView E. page ${page.hashCode()}" }
    val adapters = remember {
        DefaultAdapters
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                drawIntoCanvas { canvas ->
                    var currentX = size.width
                    for (line in page.lines) {
                        currentX -= line.lineHeight / 2
                        canvas.nativeCanvas.drawAozoraLine(currentX, line, adapters, textColor)
                        currentX -= line.lineHeight / 2
                    }
                }
                drawContent()
            }
    )
}

fun Canvas.drawAozoraLine(
    x: Float,
    line: ReaderLine,
    adapters: List<ElementRenderAdapter>,
    textColor: Int
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
                DefaultPaintProvider().getDebugPaint()
            )
        }

        currentY += drawSize.height
    }
}

@Preview
@Composable
private fun PageViewPreview() {
    val dummyPage = AozoraPage(
        lines = listOf(
            ReaderLine(
                lineHeight = 100f,
                fontStyle = FontStyle(
                    baseSize = 32f,
                    notationSize = 12f,
                    lineHeightMultiplier = 1.3f,
                    fontType = FontType.DEFAULT,
                ),
                elements = listOf(
                    AozoraElement.Indent(count = 1),
                    AozoraElement.Text(
                        text = "この台所にては毎日平均五十人前以上の",
                    ),
                    AozoraElement.Text(
                        text = "なるべくなら動かずに",
                    ),
                    AozoraElement.PageBreak,
                ).toImmutableList()
            ),
            ReaderLine(
                lineHeight = 100f,
                fontStyle = FontStyle(
                    baseSize = 32f,
                    notationSize = 12f,
                    lineHeightMultiplier = 1.3f,
                    fontType = FontType.DEFAULT,
                ),
                elements = listOf(
                    AozoraElement.LineBreak,
                ).toImmutableList()
            )
        ).toImmutableList()
    )
    Box(
        modifier = Modifier
            .size(300.dp, 500.dp)
            .background(color = Color.Yellow.copy(alpha = 0.5f))
    ) {
        PageView(
            modifier = Modifier.fillMaxSize(),
            page = dummyPage,
            textColor = Color.White.toArgb()
        )
    }
}