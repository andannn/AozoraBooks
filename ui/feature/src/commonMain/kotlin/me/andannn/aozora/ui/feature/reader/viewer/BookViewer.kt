package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.pagesource.page.builder.layout
import me.andannn.aozora.ui.common.theme.NotoSerifJpFontFamily
import me.andannn.aozora.ui.common.theme.getBackgroundColor
import me.andannn.aozora.ui.common.theme.getTextColor

private const val TAG = "BookViewer"

@Composable
fun BookViewer(state: BookViewerState, modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) {
        ReaderContent(
            modifier = Modifier.padding(it),
            pages = state.pages,
            theme = state.theme,
            pagerState = state.pagerState,
            pageMetadata = state.pageMetadata
        )
    }
}

@Composable
private fun ReaderContent(
    modifier: Modifier = Modifier,
    pages: List<AozoraPage>,
    theme: ReaderTheme,
    pagerState: PagerState,
    pageMetadata: PageMetaData
) {
    val backgroundColor = theme.getBackgroundColor(MaterialTheme.colorScheme)
    val textColor = theme.getTextColor(MaterialTheme.colorScheme)
    val fontFamily = getFontFamilyByType(pageMetadata.fontType)

    Napier.d(tag = TAG) { "ReaderContent font style ${pageMetadata.fontType}, fontFamily $fontFamily" }
    HorizontalPager(
        modifier = Modifier.background(backgroundColor),
        state = pagerState,
        reverseLayout = true,
    ) { pageIndex ->
        val page = rememberUpdatedState(pages.getOrNull(pageIndex))
        val layoutPage = remember(page.value) {
            page.value?.layout()
        }
        if (layoutPage != null) {
            PageViewV2(
                modifier = Modifier.fillMaxSize(),
                page = layoutPage,
                textColor = textColor,
                fontFamily = fontFamily
            )
        }
    }
}

@Composable
private fun getFontFamilyByType(type: FontType): FontFamily {
    return when (type) {
        FontType.NOTO_SANS -> FontFamily.Default
        FontType.NOTO_SERIF -> NotoSerifJpFontFamily
    }
}
