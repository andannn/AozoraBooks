/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.overlay

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.slack.circuit.retained.rememberRetained
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import me.andannn.aozora.ui.common.util.toPercentString
import kotlin.math.roundToInt

private const val TAG = "ReaderOverlay"

@Composable
fun ReaderOverlay(
    state: ReaderOverlayState,
    modifier: Modifier = Modifier,
) {
    ReaderOverlayContent(
        modifier = modifier,
        pagerState = state.pagerState,
        showOverlay = state.showOverlay,
        onEvent = state.eventSink,
    )
}

@Composable
fun ReaderOverlayContent(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    showOverlay: Boolean,
    onEvent: (ReaderOverlayEvent) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val pageSize = pagerState.pageCount
    val settledPageIndex = pagerState.targetPage
    Column(
        modifier = modifier,
    ) {
        val animatedAlpha = animateFloatAsState(targetValue = if (showOverlay) 1f else 0f)

        val density = LocalDensity.current
        val statusBarHeightPx = WindowInsets.statusBars.getTop(density)
        val navigationBarHeightPx = WindowInsets.navigationBars.getBottom(density)
        val statusBarHeight =
            rememberRetained {
                with(density) { statusBarHeightPx.toDp() }
            }
        val navigationBarHeight =
            rememberRetained {
                with(density) { navigationBarHeightPx.toDp() }
            }

        OverlayTopBar(
            modifier =
                Modifier
                    .graphicsLayer {
                        alpha = animatedAlpha.value
                    }.background(MaterialTheme.colorScheme.surface)
                    .padding(top = statusBarHeight),
            enable = animatedAlpha.value != 0f,
            onBack = {
                onEvent.invoke(ReaderOverlayEvent.OnBack)
            },
            onClickTableOfContents = {
                onEvent.invoke(ReaderOverlayEvent.OnOpenTableOfContents)
            },
            onClickSetting = {
                onEvent.invoke(ReaderOverlayEvent.OnOpenFontSetting)
            },
        )

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            modifier =
                Modifier
                    .graphicsLayer {
                        alpha = animatedAlpha.value
                    },
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(24.dp)
                        .padding(bottom = navigationBarHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = (settledPageIndex.toFloat() / (pageSize - 1)).toPercentString(),
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgressSlider(
                    modifier = Modifier,
                    pageSize = pageSize,
                    enable = animatedAlpha.value != 0f,
                    currentPageIndex = settledPageIndex,
                    onPageChanged = {
                        scope.launch {
                            Napier.d(tag = TAG) { "on Change it. $it" }
                            pagerState.scrollToPage(it)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun OverlayTopBar(
    modifier: Modifier = Modifier,
    enable: Boolean,
    onBack: () -> Unit = {},
    onClickTableOfContents: () -> Unit = {},
    onClickSetting: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row {
            IconButton(
                onClick = {
                    onBack()
                },
                enabled = enable,
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onClickTableOfContents()
                },
                enabled = enable,
            ) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = {
                    onClickSetting()
                },
                enabled = enable,
            ) {
                Icon(Icons.Filled.Settings, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun ProgressSlider(
    modifier: Modifier = Modifier,
    enable: Boolean,
    pageSize: Int,
    currentPageIndex: Int,
    onPageChanged: (Int) -> Unit,
) {
    if (pageSize >= 2) {
        Slider(
            modifier =
                modifier
                    .graphicsLayer { scaleX = -1f },
            enabled = enable,
            value = currentPageIndex.toFloat(),
            valueRange = 0f..(pageSize - 1).toFloat(),
            onValueChange = {
                onPageChanged(it.roundToInt())
            },
        )
    }
}
