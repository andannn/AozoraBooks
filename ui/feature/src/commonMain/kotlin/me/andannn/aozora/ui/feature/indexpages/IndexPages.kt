/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.indexpages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import me.andannn.aozora.core.data.common.BookColumnItem
import me.andannn.aozora.ui.common.widgets.AdType
import me.andannn.aozora.ui.common.widgets.BannerAdView

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
    pagingData: LazyPagingItems<BookColumnItem>,
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
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
                    }
                },
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
        ) {
            items(pagingData.itemCount) { index ->
                Column {
                    pagingData[index]?.let {
                        BookColumnItemView(
                            modifier = Modifier.fillMaxWidth(),
                            item = it,
                            onClick = {
                                onEvent.invoke(IndexPagesUiEvent.OnBookClick(it))
                            },
                        )
                        HorizontalDivider()
                    }

                    if (index % 30 == 0) {
                        BannerAdView(
                            modifier = Modifier.fillMaxWidth(),
                            adType = AdType.LEADERBOARD,
                        )
                        HorizontalDivider()
                    }
                }
            }

            if (pagingData.loadState.append is LoadState.Loading) {
                item {
                    Column {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BookColumnItemView(
    item: BookColumnItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Surface(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text("No.${item.index}", style = MaterialTheme.typography.labelLarge)
            Text(
                item.title.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            if (item.title.subTitle != null) {
                Text(item.title.subTitle!!, style = MaterialTheme.typography.bodyMedium)
            }
            Text("著者：" + item.author, style = MaterialTheme.typography.bodySmall)
            if (item.translator != null) {
                Text("訳者：" + item.translator!!, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
