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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import me.andannn.aozora.core.domain.model.Line
import me.andannn.aozora.core.domain.model.Page
import me.andannn.aozora.core.domain.model.PageMetaData
import me.andannn.aozora.ui.common.theme.RandomColor
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.RenderAdapterProvider
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.drawWithAdapter

@Composable
fun TextPageViewV2(
    pageMetaData: PageMetaData,
    page: Page.TextLayoutPage,
    textColor: Color,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
) {
    val measurer =
        rememberTextMeasurer(
            cacheSize = 0,
        )
    val density = LocalDensity.current
    val adapterProvider =
        remember(measurer, density, fontFamily, textColor) {
            RenderAdapterProvider(measurer, density, fontFamily, textColor)
        }
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .drawWithCache {
                    val contentWidthDp = page.contentWidth
                    val renderWidthDp = pageMetaData.renderWidth
                    val renderHeightDp = pageMetaData.renderHeight
                    val offsetXDp = pageMetaData.offset.first
                    val offsetYDp = pageMetaData.offset.second
                    val contentWidth = with(density) { contentWidthDp.toPx() }
                    val renderWidth = with(density) { renderWidthDp.toPx() }
                    val renderHeight = with(density) { renderHeightDp.toPx() }
                    val offsetX = with(density) { offsetXDp.toPx() }
                    val offsetY = with(density) { offsetYDp.toPx() }

                    onDrawBehind {
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
                                    val lineHeightPx =
                                        with(density) { line.line.lineHeight.toPx() }
                                    currentX -= lineHeightPx / 2
                                    drawAozoraLineV2(currentX, line.line, adapterProvider, density)
                                    currentX -= lineHeightPx / 2
                                }
                            }
                        }
                    }
                },
    )
}

private fun DrawScope.drawAozoraLineV2(
    x: Float,
    line: Line,
    adapterProvider: RenderAdapterProvider,
    density: Density,
) {
    var currentY = 0f
    line.elements.forEach { item ->
        val fontStyle = item.fontStyle
        var drawSize: Size? = null
        val size = drawWithAdapter(adapterProvider, x, currentY, item.element, fontStyle)
        if (size != null) {
            drawSize = size
        }
        if (drawSize == null) {
            error("No adapter can draw element $item")
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
