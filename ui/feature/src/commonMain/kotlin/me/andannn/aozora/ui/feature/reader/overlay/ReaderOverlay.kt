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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
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

        OverlayTopBar(
            modifier =
                Modifier
                    .graphicsLayer {
                        alpha = animatedAlpha.value
                    }.background(MaterialTheme.colorScheme.surface)
                    .statusBarsPadding(),
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

        ProgressSlider(
            modifier =
                Modifier
                    .graphicsLayer {
                        alpha = animatedAlpha.value
                    }.background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
            pageSize = pageSize,
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

@Composable
private fun OverlayTopBar(
    modifier: Modifier = Modifier,
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
            ) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
            }

            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onClickTableOfContents()
                },
            ) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
            IconButton(
                onClick = {
                    onClickSetting()
                },
            ) {
                Icon(Icons.Filled.Settings, contentDescription = null)
            }
        }
    }
}

@Composable
private fun ProgressSlider(
    modifier: Modifier = Modifier,
    pageSize: Int,
    currentPageIndex: Int,
    onPageChanged: (Int) -> Unit,
) {
    if (pageSize >= 2) {
        Slider(
            modifier =
                modifier
                    .graphicsLayer { scaleX = -1f },
            value = currentPageIndex.toFloat(),
            valueRange = 0f..(pageSize - 1).toFloat(),
            onValueChange = {
                onPageChanged(it.roundToInt())
            },
        )
    }
}
