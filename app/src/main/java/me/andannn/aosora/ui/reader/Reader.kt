package me.andannn.aosora.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.ReaderTheme
import me.andannn.aosora.core.common.model.getBackgroundColor
import me.andannn.aosora.core.common.model.getTextColor

@Composable
fun Reader(state: ReaderState, modifier: Modifier = Modifier) {
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
    pages: ImmutableList<AozoraPage>,
    theme: ReaderTheme,
    pagerState: PagerState
) {
    val backgroundColor = theme.getBackgroundColor(MaterialTheme.colorScheme)
    val textColor = theme.getTextColor(MaterialTheme.colorScheme).toArgb()
    val state = remember {
        mutableIntStateOf(0)
    }

    HorizontalPager(
        modifier = Modifier.background(backgroundColor),
        state = pagerState,
        reverseLayout = true,
    ) { pageIndex ->
        PageView(
            modifier = Modifier.fillMaxSize(),
            page = pages[pageIndex],
            textColor = textColor
        )
    }
}

private const val TAG = "Reader1"
/**
 * Creates a [PagerState] that can be re-initialized when initialPage changed.
 */
@Composable
fun rememberRefreshablePagerState(
    initialPage: Int = 0,
    pageCount: () -> Int
): PagerState {
    return rememberSaveable(initialPage, saver = DefaultPagerState.Saver) {
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
