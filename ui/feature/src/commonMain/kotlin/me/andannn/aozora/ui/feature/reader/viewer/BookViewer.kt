package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Slider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.pagesource.page.builder.layout
import me.andannn.aozora.ui.common.theme.NotoSerifJpFontFamily
import me.andannn.aozora.ui.common.theme.getBackgroundColor
import me.andannn.aozora.ui.common.theme.getTextColor
import me.andannn.aozora.ui.feature.reader.viewer.page.AozoraBibliographicalPage
import me.andannn.aozora.ui.feature.reader.viewer.page.PageViewV2
import kotlin.math.roundToInt

private const val TAG = "BookViewer"

@Composable
fun BookViewer(
    state: BookViewerState,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) {
        ReaderContent(
            modifier = Modifier.padding(it),
            pages = state.pages,
            theme = state.theme,
            pagerState = state.pagerState,
            pageMetadata = state.pageMetadata,
        )
    }
}

@Composable
private fun ReaderContent(
    modifier: Modifier = Modifier,
    pages: ImmutableList<AozoraPage>,
    theme: ReaderTheme,
    pagerState: PagerState,
    pageMetadata: PageMetaData,
) {
    val backgroundColor = theme.getBackgroundColor(MaterialTheme.colorScheme)
    val textColor = theme.getTextColor(MaterialTheme.colorScheme)
    val fontFamily = getFontFamilyByType(pageMetadata.fontType)
    val scope = rememberCoroutineScope()
    Napier.d(tag = TAG) { "ReaderContent font style ${pageMetadata.fontType}, fontFamily $fontFamily" }
    val pageSize = pagerState.pageCount
    val settledPageIndex = pagerState.targetPage
    Box {
        HorizontalPager(
            modifier = Modifier.background(backgroundColor),
            state = pagerState,
            reverseLayout = true,
        ) { pageIndex ->
            val pageState = rememberUpdatedState(pages.getOrNull(pageIndex))

            val page = pageState.value
            if (page is AozoraPage.AozoraBibliographicalPage) {
                AozoraBibliographicalPage(page = page)
            } else {
                val layoutPage =
                    remember(page) {
                        page?.layout()
                    }

                if (layoutPage != null) {
                    PageViewV2(
                        modifier = Modifier.fillMaxSize(),
                        page = layoutPage,
                        textColor = textColor,
                        fontFamily = fontFamily,
                    )
                }
            }
        }

        ProgressSlider(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
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

@Composable
private fun getFontFamilyByType(type: FontType): FontFamily =
    when (type) {
        FontType.NOTO_SANS -> FontFamily.Default
        FontType.NOTO_SERIF -> NotoSerifJpFontFamily
    }
