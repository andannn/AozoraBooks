/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import me.andannn.aozora.core.domain.layouthelper.AozoraPageLayoutHelper
import me.andannn.aozora.core.domain.model.AozoraPage
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.ui.common.theme.getBackgroundColor
import me.andannn.aozora.ui.common.theme.getFontFamilyByType
import me.andannn.aozora.ui.common.theme.getTextColor
import me.andannn.aozora.ui.feature.reader.viewer.page.AozoraBibliographicalPage
import me.andannn.aozora.ui.feature.reader.viewer.page.PageViewV2
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun BookViewer(
    state: BookViewerState,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) {
        ReaderContent(
            modifier = Modifier.padding(it),
            pages = state.bookPageState.pages,
            theme = state.theme,
            pagerState = state.bookPageState.pagerState,
            fontType = state.fontType,
        )
    }
}

@Composable
private fun ReaderContent(
    modifier: Modifier = Modifier,
    pages: ImmutableList<AozoraPage>,
    theme: ReaderTheme,
    pagerState: PagerState,
    fontType: FontType,
) {
    if (pages.isEmpty()) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        return
    }

    val backgroundColor = theme.getBackgroundColor(MaterialTheme.colorScheme)
    val textColor = theme.getTextColor(MaterialTheme.colorScheme)
    val fontFamily = getFontFamilyByType(fontType)

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        HorizontalPager(
            modifier = Modifier.background(backgroundColor),
            state = pagerState,
            reverseLayout = true,
        ) { pageIndex ->
            val pageState = rememberUpdatedState(pages.getOrNull(pageIndex))

            val page = pageState.value
            if (page is AozoraPage.AozoraBibliographicalPage) {
                AozoraBibliographicalPage(page = page, textColor = textColor)
            } else {
                val layoutHelper =
                    remember {
                        getKoin().get<AozoraPageLayoutHelper>()
                    }
                val layoutPage =
                    remember(page) {
                        with(layoutHelper) {
                            page?.layout()
                        }
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
    }
}
