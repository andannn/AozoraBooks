package me.andannn.aozora.ui.feature.reader.overlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
        onEvent = state.eventSink,
    )
}

@Composable
fun ReaderOverlayContent(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onEvent: (ReaderOverlayEvent) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val pageSize = pagerState.pageCount
    val settledPageIndex = pagerState.targetPage

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(48.dp))
        TextButton(
            onClick = {
                onEvent.invoke(ReaderOverlayEvent.OnOpenFontSetting)
            },
        ) {
            Text("Open Font Setting")
        }
        Spacer(modifier = Modifier.weight(1f))
        ProgressSlider(
            modifier =
                Modifier
                    .padding(bottom = 24.dp, start = 40.dp, end = 40.dp),
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
