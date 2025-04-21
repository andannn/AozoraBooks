package me.andannn.aosora.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aosora.core.pager.AozoraPage

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
    pages: ImmutableList<AozoraPage>
) {
    HorizontalPager(
        state = rememberPagerState(
            pageCount = { pages.size }
        ),
        reverseLayout = true,
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
    PageView(
        modifier = modifier.background(Color.Red.copy(alpha = 0.3f)),
        page = page,
    )
}
