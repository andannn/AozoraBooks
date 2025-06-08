/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.searchresult

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.andannn.aozora.ui.common.widgets.AuthorColumnItemView
import me.andannn.aozora.ui.common.widgets.BookColumnItemView

@Composable
fun SearchResult(
    state: SearchResultState,
    modifier: Modifier = Modifier,
) {
    SearchResultContent(
        modifier = modifier,
        query = state.query,
        loadState = state.loadState,
        onEvent = state.evenSink,
    )
}

@Composable
private fun SearchResultContent(
    query: String,
    loadState: LoadState,
    modifier: Modifier = Modifier,
    onEvent: (SearchResultUiEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding())) {
            Surface(
                modifier =
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = CircleShape,
                onClick = {
                    onEvent(SearchResultUiEvent.OnTitleClick)
                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(Modifier.width(4.dp))
                    IconButton(
                        onClick = {
                            onEvent(SearchResultUiEvent.Back)
                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(text = query, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            when (loadState) {
                LoadState.Loading -> {
                    Box(modifier = Modifier.weight(1f)) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is LoadState.Result -> {
                    LazyColumn {
                        if (loadState.authors.isNotEmpty()) {
                            item {
                                Text(
                                    modifier = Modifier.padding(12.dp),
                                    text = "作者",
                                    style = MaterialTheme.typography.headlineSmall,
                                )
                            }
                            items(loadState.authors.size) { index ->
                                val item = loadState.authors[index]
                                AuthorColumnItemView(
                                    index = null,
                                    item = item,
                                    onClick = {
                                        onEvent(
                                            SearchResultUiEvent.OnAuthorClick(item),
                                        )
                                    },
                                )
                            }
                        }

                        if (loadState.authors.isNotEmpty() && loadState.books.isNotEmpty()) {
                            item {
                                HorizontalDivider()
                            }
                        }

                        if (loadState.books.isNotEmpty()) {
                            item {
                                Text(
                                    modifier = Modifier.padding(12.dp),
                                    text = "作品",
                                    style = MaterialTheme.typography.headlineSmall,
                                )
                            }
                            items(loadState.books.size) { index ->
                                val item = loadState.books[index]
                                BookColumnItemView(
                                    item = item,
                                    index = null,
                                    onClick = {
                                        onEvent(
                                            SearchResultUiEvent.OnBookClick(item),
                                        )
                                    },
                                )
                            }
                        }
                    }
                }

                LoadState.NoResult -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp),
                            text = "作家や作品が見つかりませんでした。キーワードを変えてもう一度お試しください。",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}
