/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer.page

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.domain.model.LayoutPage
import me.andannn.aozora.core.domain.model.Line
import me.andannn.aozora.ui.common.theme.RandomColor
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.ElementRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.createAdapters

private const val TAG = "PageView"

@Composable
fun PageViewV2(
    page: LayoutPage,
    textColor: Color,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
) {
    Napier.d(tag = TAG) { "PageView E. page $page" }
    val measurer =
        rememberTextMeasurer(
            cacheSize = 0,
        )
    val density = LocalDensity.current
    val adapters =
        remember(measurer, density, fontFamily, textColor) {
            createAdapters(measurer, density, fontFamily, textColor)
        }
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .drawWithContent {
                    val contentWidthDp = page.contentWidth
                    val renderWidthDp = page.pageMetaData.renderWidth
                    val renderHeightDp = page.pageMetaData.renderHeight
                    val offsetXDp = page.pageMetaData.offset.first
                    val offsetYDp = page.pageMetaData.offset.second
                    val contentWidth = with(density) { contentWidthDp.toPx() }
                    val renderWidth = with(density) { renderWidthDp.toPx() }
                    val renderHeight = with(density) { renderHeightDp.toPx() }
                    val offsetX = with(density) { offsetXDp.toPx() }
                    val offsetY = with(density) { offsetYDp.toPx() }

                    if (DEBUG_RENDER) {
                        drawRect(
                            topLeft = Offset(offsetX, offsetY),
                            size = Size(renderWidth, renderHeight),
                            color = RandomColor,
                        )
                    }
                    translate(
                        left = (contentWidth - renderWidth).div(2),
                    ) {
                        translate(
                            offsetX,
                            offsetY,
                        ) {
                            var currentX = renderWidth
                            for (line in page.lines) {
                                val lineHeightPx = with(density) { line.lineHeight.toPx() }
                                currentX -= lineHeightPx / 2
                                drawAozoraLineV2(currentX, line, adapters, density)
                                currentX -= lineHeightPx / 2
                            }
                        }
                    }
                },
    )
}

fun DrawScope.drawAozoraLineV2(
    x: Float,
    line: Line,
    adapters: List<ElementRenderAdapterV2>,
    density: Density,
) {
    val fontStyle = line.fontStyle
    var currentY = 0f
    line.elements.forEach { element ->
        var drawSize: Size? = null
        for (adapter in adapters) {
            val size = with(adapter) { draw(x, currentY, element, fontStyle) }
            if (size != null) {
                drawSize = size
                break
            }
        }
        if (drawSize == null) {
            error("No adapter can draw element $element")
        }

        if (DEBUG_RENDER) {
            val lineHeightPx = with(density) { line.lineHeight.toPx() }
            drawRect(
                topLeft =
                    Offset(
                        x - lineHeightPx / 2,
                        currentY,
                    ),
                size =
                    Size(
                        lineHeightPx,
                        drawSize.height,
                    ),
                color = RandomColor,
            )
        }

        currentY += drawSize.height
    }
}
