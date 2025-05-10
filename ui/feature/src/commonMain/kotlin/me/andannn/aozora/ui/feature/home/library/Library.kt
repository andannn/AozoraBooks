/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.data.common.BookPreviewInfo
import me.andannn.aozora.ui.common.widgets.PreviewBookCard

@Composable
fun Library(
    state: LibraryState,
    modifier: Modifier = Modifier,
) {
    LibraryContent(
        modifier = modifier,
        onEvent = state.evenSink,
    )
}

@Composable
fun LibraryContent(
    modifier: Modifier,
    onEvent: (LibraryUiEvent) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp),
        columns = GridCells.Adaptive(128.dp),
    ) {
        items(
            items = bookCardList,
            key = { it.id },
        ) { card ->
            PreviewBookCard(
                author = card.authorName.toString(),
                title = card.title,
                onClick = {
                    onEvent.invoke(LibraryUiEvent.OnCardClick(card.id))
                },
            )
        }
    }
}

val bookCardList =
    listOf<BookPreviewInfo>(
        BookPreviewInfo(
            id = "301",
            title = "人間失格",
            authorName = "太宰治",
            zipUrl = "https://www.aozora.gr.jp/cards/000035/files/301_ruby_5915.zip",
            htmlUrl = "https://www.aozora.gr.jp/cards/000035/files/301_14912.html",
            bookCardUrl = "",
        ),
        BookPreviewInfo(
            id = "789",
            title = "吾輩は猫である",
            authorName = "夏目漱石",
            zipUrl = "https://www.aozora.gr.jp/cards/000148/files/789_ruby_5639.zip",
            htmlUrl = "https://www.aozora.gr.jp/cards/000148/files/789_14547.html",
            bookCardUrl = "",
        ),
        BookPreviewInfo(
            id = "60756",
            title = "現代語訳　平家物語",
            authorName = "宮沢賢治",
            zipUrl = "https://www.aozora.gr.jp/cards/001529/files/60756_ruby_74753.zip",
            htmlUrl = "https://www.aozora.gr.jp/cards/001529/files/60756_74787.html",
            bookCardUrl = "",
        ),
    )
