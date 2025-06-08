/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.author

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.ui.common.widgets.BookColumnItemView
import me.andannn.aozora.ui.common.widgets.ClickableOrText
import me.andannn.aozora.ui.common.widgets.Heading
import me.andannn.aozora.ui.common.widgets.ItemRow
import me.andannn.core.util.removePrefixRecursive

@Composable
fun Author(
    state: AuthorState,
    modifier: Modifier = Modifier,
) {
    AuthorContent(
        modifier = modifier,
        authorId = state.authorId,
        author = state.authorData?.author,
        books = state.authorData?.books ?: emptyList(),
        onEvent = state.evenSink,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorContent(
    modifier: Modifier = Modifier,
    authorId: String,
    author: AuthorData?,
    books: List<AozoraBookCard>,
    onEvent: (AuthorUiEvent) -> Unit = {},
) {
    Scaffold(
        modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("作家別作品リスト：No.${authorId.removePrefixRecursive("0")}")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(AuthorUiEvent.OnBack)
                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        if (author == null) {
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
        ) {
            item {
                Row(
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 12.dp),
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "作家名：",
                        color = MaterialTheme.colorScheme.inverseSurface,
                    )
                    ClickableOrText(
                        modifier = Modifier.weight(2f),
                        value = author.authorName,
                        textStyle = MaterialTheme.typography.headlineMedium,
                    )
                }

                author.authorNameKana?.let {
                    ItemRow(title = "作家名読み：", value = it)
                }
                author.authorNameRomaji?.let {
                    ItemRow(title = "ローマ字表記：", value = it)
                }
                author.birth?.let {
                    ItemRow(title = "生年：", value = it)
                }
                author.death?.let {
                    ItemRow(title = "没年：", value = it)
                }
            }

            item {
                Heading(
                    text = "公開中の作品",
                )
            }

            items(items = books, key = { item -> item.id }) { card ->
                BookColumnItemView(
                    modifier = Modifier.fillMaxWidth(),
                    item = card,
                    index = books.indexOf(card),
                    onClick = {
                        onEvent(
                            AuthorUiEvent.OnBookClick(
                                book = card,
                            ),
                        )
                    },
                )
            }
        }
    }
}
