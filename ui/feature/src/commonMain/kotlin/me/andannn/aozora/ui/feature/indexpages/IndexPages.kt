/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.indexpages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.ui.common.widgets.BannerAdView
import me.andannn.aozora.ui.common.widgets.BookColumnItemView
import me.andannn.aozora.ui.common.widgets.NavigationBarAnchor
import me.andannn.platform.AdType
import me.andannn.platform.showPlatformAd

@Composable
fun IndexPages(
    state: IndexPagesState,
    modifier: Modifier = Modifier,
) {
    IndexPagesStateContent(
        modifier = modifier,
        pagingData = state.pagingData,
        label = state.kanaLabel,
        onEvent = state.evenSink,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexPagesStateContent(
    modifier: Modifier = Modifier,
    label: String,
    pagingData: LazyPagingItems<AozoraBookCard>,
    onEvent: (IndexPagesUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("作品一覧：$label")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent.invoke(IndexPagesUiEvent.OnBack)
                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
        ) {
            items(pagingData.itemCount) { index ->
                Column {
                    pagingData[index]?.let {
                        BookColumnItemView(
                            modifier = Modifier.fillMaxWidth(),
                            index = index,
                            item = it,
                            onClick = {
                                onEvent.invoke(IndexPagesUiEvent.OnBookClick(it))
                            },
                        )
                        HorizontalDivider()
                    }

                    if (index % 20 == 0 && showPlatformAd) {
                        BannerAdView(
                            modifier = Modifier.fillMaxWidth(),
                            adType = AdType.LEADERBOARD,
                        )
                        HorizontalDivider()
                    }
                }
            }

            item {
                NavigationBarAnchor()
            }
        }
    }
}
