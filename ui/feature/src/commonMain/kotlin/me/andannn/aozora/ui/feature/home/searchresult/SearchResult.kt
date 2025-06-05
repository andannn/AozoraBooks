/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.searchresult

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultContent(
    query: String,
    loadState: LoadState,
    modifier: Modifier = Modifier,
    onEvent: (SearchResultUiEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent,
                        onClick = {
                            onEvent(SearchResultUiEvent.OnTitleClick)
                        },
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Text(query)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SearchResultUiEvent.Back) }) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
            )
        },
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            when (loadState) {
                LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
