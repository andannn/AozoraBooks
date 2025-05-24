/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.BookWithProgress
import me.andannn.aozora.ui.common.widgets.PreviewBookCard

@Composable
fun Library(
    state: LibraryState,
    modifier: Modifier = Modifier,
) {
    LibraryContent(
        modifier = modifier,
        notCompletedBooks = state.notCompletedBooks,
        completedBooks = state.completedBooks,
        currentTab = state.currentTab,
        onEvent = state.evenSink,
    )
}

@Composable
fun LibraryContent(
    modifier: Modifier,
    currentTab: TabItem,
    notCompletedBooks: List<BookWithProgress>,
    completedBooks: List<BookWithProgress>,
    onEvent: (LibraryUiEvent) -> Unit,
) {
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = currentTab.ordinal) {
            TabItem.entries.forEach {
                Tab(
                    selected = currentTab == it,
                    onClick = {
                        onEvent.invoke(LibraryUiEvent.OnTabRowClick(it))
                    },
                    text = {
                        Text(it.label())
                    },
                )
            }
        }

        if (currentTab == TabItem.READING) {
            if (notCompletedBooks.isEmpty()) {
                Column(
                    Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        "まだ読んでいる本がありません。\n気になる本を探して追加してみましょう。",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            onEvent.invoke(LibraryUiEvent.OnGoToSearch)
                        },
                    ) {
                        Text("本を検索する")
                    }
                }
            } else {
                BookList(
                    modifier = Modifier,
                    bookList = notCompletedBooks,
                    onEvent = onEvent,
                )
            }
        } else {
            if (completedBooks.isEmpty()) {
                Column(
                    Modifier.fillMaxSize().padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        "閲覧済みの本はありません。",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                BookList(
                    modifier = Modifier,
                    bookList = completedBooks,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun BookList(
    modifier: Modifier,
    bookList: List<BookWithProgress>,
    onEvent: (LibraryUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 18.dp),
    ) {
        items(
            items = bookList,
            key = { it.book.id },
        ) { bookWithProgress ->
            val card = bookWithProgress.book
            val progress = bookWithProgress.progress

            PreviewBookCard(
                modifier = Modifier.animateItem(),
                title = card.title,
                author = card.authorName.toString(),
                progress = progress,
                userMarkRead = bookWithProgress.isUserMarkCompleted,
                onClick = {
                    onEvent.invoke(LibraryUiEvent.OnCardClick(card.id))
                },
                onOptionClick = {
                    onEvent.invoke(LibraryUiEvent.OnCardOptionClick(card))
                },
            )
        }
    }
}

private fun TabItem.label(): String =
    when (this) {
        TabItem.READING -> "読書中"
        TabItem.READ_COMPLETE -> "読了"
    }
