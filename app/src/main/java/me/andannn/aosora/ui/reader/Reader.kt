package me.andannn.aosora.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import kotlinx.collections.immutable.ImmutableList
import me.andannn.aosora.core.common.model.ReaderTheme
import me.andannn.aosora.core.common.model.getBackgroundColor
import me.andannn.aosora.core.common.model.getTextColor
import me.andannn.aosora.core.pager.AozoraPage

@Composable
fun Reader(state: ReaderState, modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) {
        ReaderContent(
            modifier = Modifier.padding(it),
            pages = state.pages,
            theme = state.theme
        )
    }
}

@Composable
private fun ReaderContent(
    modifier: Modifier = Modifier,
    pages: ImmutableList<AozoraPage>,
    theme: ReaderTheme,
) {
    val backgroundColor = theme.getBackgroundColor(MaterialTheme.colorScheme)
    val textColor = theme.getTextColor(MaterialTheme.colorScheme).toArgb()
    HorizontalPager(
        modifier = Modifier.background(backgroundColor),
        state = rememberPagerState(
            pageCount = { pages.size }
        ),
        reverseLayout = true,
    ) { pageIndex ->
        PageView(
            modifier = Modifier.fillMaxSize(),
            page = pages[pageIndex],
            textColor = textColor
        )
    }
}
