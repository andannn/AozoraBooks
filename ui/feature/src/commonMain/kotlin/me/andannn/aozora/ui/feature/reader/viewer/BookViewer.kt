package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.pagesource.page.builder.layout
import me.andannn.aozora.ui.common.theme.getBackgroundColor
import me.andannn.aozora.ui.common.theme.getTextColor

@Composable
fun BookViewer(state: BookViewerState, modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) {
        ReaderContent(
            modifier = Modifier.padding(it),
            pages = state.pages,
            theme = state.theme,
            pagerState = state.pagerState
        )
    }
}

@Composable
private fun ReaderContent(
    modifier: Modifier = Modifier,
    pages: List<AozoraPage>,
    theme: ReaderTheme,
    pagerState: PagerState
) {
    val backgroundColor = theme.getBackgroundColor(MaterialTheme.colorScheme)
    val textColor = theme.getTextColor(MaterialTheme.colorScheme)

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
                textColor = textColor
            )
        }
    }
}

private const val TAG = "Reader1"
/**
 * Creates a [PagerState] that can be re-initialized when initialPage changed.
 */
@Composable
fun rememberRefreshablePagerState(
    initialPage: Int = 0,
    version: Int?,
    pageCount: () -> Int
): PagerState {
    return rememberSaveable(initialPage, version, saver = DefaultPagerState.Saver) {
        Napier.d(tag = TAG) { "create new pager state: initialPage: $initialPage" }
        DefaultPagerState(
            initialPage,
            pageCount
        )
    }.apply {
        pageCountState.value = pageCount
    }
}

private class DefaultPagerState(
    currentPage: Int,
    updatedPageCount: () -> Int
) : PagerState(currentPage) {

    var pageCountState = mutableStateOf(updatedPageCount)
    override val pageCount: Int get() = pageCountState.value.invoke()

    companion object {
        /**
         * To keep current page and current page offset saved
         */
        val Saver: Saver<DefaultPagerState, *> = listSaver(
            save = {
                listOf(
                    it.currentPage,
                    it.pageCount
                )
            },
            restore = {
                DefaultPagerState(
                    currentPage = it[0] as Int,
                    updatedPageCount = { it[1] as Int }
                )
            }
        )
    }
}
