package me.andannn.aosora.ui.reader

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.VERTICAL_TEXT_FLAG
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import me.andannn.aosora.core.pager.AozoraPage
import me.andannn.aosora.core.pager.ReaderLine
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.render.ElementRenderAdapter
import me.andannn.aosora.core.render.ElementRenderAdapter.Companion.DefaultAdapters
import me.andannn.aosora.ui.reader.drawAozoraLine

@Composable
fun Reader(state: ReaderState, modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) {
        ReaderContent(
            modifier = Modifier.padding(it),
            pages = state.pages
        )
    }
}

@Composable
private fun ReaderContent(
    modifier: Modifier = Modifier,
    pages: List<AozoraPage>
) {
    HorizontalPager(
        state = rememberPagerState(
            pageCount = { pages.size }
        ),
    ) { pageIndex ->
        SinglePage(
            modifier = Modifier.fillMaxSize(),
            page = pages[pageIndex]
        )
    }
}

@Composable
fun SinglePage(
    modifier: Modifier = Modifier,
    page: AozoraPage
) {
    AozoraPageRender(
        modifier = modifier.background(Color.Red.copy(alpha = 0.3f)),
        page = page,
    )
}

@Composable
fun AozoraPageRender(
    modifier: Modifier = Modifier,
    page: AozoraPage,
) {
    val text = "春は、曙"
    val adapters = remember {
        DefaultAdapters
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                drawIntoCanvas { canvas ->
//                    val textFontSize = 64f
//                    val paint = Paint().apply { textSize = textFontSize }
//                    // Draw text vertically
//                    paint.flags = paint.flags or VERTICAL_TEXT_FLAG
//                    val height = paint.measureText(text)
//                    canvas.nativeCanvas.drawText(
//                        /* text = */ text,
//                        /* start = */ 0,
//                        /* end = */ text.length,
//                        /* x = */ size.width / 2,
//                        /* y = */ (size.height - height) / 2,
//                        /* paint = */ paint
//                    )
//                    canvas.nativeCanvas.drawPoint(
//                        size.width / 2,
//                        (size.height - height) / 2 + height,
//                        Paint().apply {
//                            color = Color.Red.hashCode()
//                            strokeWidth = 10f
//                        }
//                    )
//                    canvas.nativeCanvas.drawRect(
//                        size.width / 2 - textFontSize /2,
//                        (size.height) / 2 - height / 2,
//                        size.width / 2 + textFontSize /2,
//                        (size.height) / 2 + height / 2,
//                        Paint().apply {
//                            color = Color.Red.copy(alpha = 0.3f).hashCode()
//                            strokeWidth = 10f
//                        }
//                    )

                    var currentX = size.width
                    for (line in page.lines) {
                        currentX -= line.lineHeight / 2
                        canvas.nativeCanvas.drawAozoraLine(currentX, line, adapters)
                        currentX -= line.lineHeight / 2
                    }
                }
            }
    )
}

fun Canvas.drawAozoraLine(x: Float, line: ReaderLine, adapters: List<ElementRenderAdapter>) {
    val fontStyle = line.fontStyle
    var currentY = 0f
    line.elements.forEach { element ->
        var drawSize: Size? = null
        for (adapter in adapters) {
            try {
                drawSize = adapter.draw(this, x, currentY, element, fontStyle)
            } catch (e: Exception) {

            }
            break
        }
        currentY += drawSize?.height ?: 0f
    }
}