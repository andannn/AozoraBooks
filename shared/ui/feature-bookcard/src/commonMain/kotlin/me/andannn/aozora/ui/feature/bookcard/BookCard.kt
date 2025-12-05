/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.bookcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.ui.common.Presenter
import me.andannn.aozora.ui.common.widgets.BannerAdView
import me.andannn.aozora.ui.common.widgets.ClickableOrText
import me.andannn.aozora.ui.common.widgets.Heading
import me.andannn.aozora.ui.common.widgets.ItemRow
import me.andannn.aozora.ui.common.widgets.NavigationBarAnchor
import me.andannn.platform.AdType
import me.andannn.platform.showPlatformAd

private const val TAG = "BookCard"

@Composable
fun BookCard(
    groupId: String,
    bookId: String,
    presenter: Presenter<BookCardState> =
        retainBookCardPresenter(
            groupId = groupId,
            bookId = bookId,
        ),
    modifier: Modifier = Modifier,
) {
    BookCard(
        state = presenter.present(),
        modifier = modifier,
    )
}

@Composable
private fun BookCard(
    state: BookCardState,
    modifier: Modifier = Modifier,
) {
    BookCardContent(
        modifier = modifier,
        bookCardInfo = state.bookCardInfo,
        ndcClassificationToDescription = state.ndcClassificationToDescription,
        isAddedToShelf = state.isAddedToShelf,
        onEvent = state.evenSink,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookCardContent(
    modifier: Modifier = Modifier,
    bookCardInfo: AozoraBookCard?,
    ndcClassificationToDescription: Map<NDCClassification, String>,
    isAddedToShelf: Boolean,
    onEvent: (BookCardUiEvent) -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current
    if (bookCardInfo == null) {
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

    Scaffold(
        modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "図書カード：No.${bookCardInfo.id}",
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(BookCardUiEvent.Back)
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
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
                    Column(
                        modifier = Modifier.align(Alignment.Center).width(IntrinsicSize.Max),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = bookCardInfo.titleKana,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = bookCardInfo.title,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        ClickableOrText(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = bookCardInfo.author ?: "null",
                            onClick = {
                                onEvent.invoke(
                                    BookCardUiEvent.OnClickAuthor(bookCardInfo.authorId),
                                )
                            },
                        )
                    }
                }
            }

            item {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onEvent.invoke(BookCardUiEvent.OnAddToShelf)
                        },
                    ) {
                        if (isAddedToShelf) {
                            Text("本棚から削除")
                        } else {
                            Text("本棚に追加")
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    FilledIconButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onEvent.invoke(BookCardUiEvent.OnClickRead)
                        },
                    ) {
                        Text("読む")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }

            item {
                Heading(text = "作品データ")
            }

            item {
                NdcCategoryRow(
                    title = "分類：",
                    ndcClassificationToDescription = ndcClassificationToDescription,
                    onNdcClick = { ndcClassification ->
                        onEvent.invoke(BookCardUiEvent.OnNdcClick(ndcClassification))
                    },
                )
                bookCardInfo.source?.let {
                    ItemRow(title = "初出：", value = it)
                }
                bookCardInfo.characterType?.let {
                    ItemRow(title = "文字遣い種別：", value = it)
                }
                bookCardInfo.takeIf { it.haveCopyRight }?.let {
                    Text(
                        modifier =
                            modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp, horizontal = 12.dp),
                        text = "＊著作権存続＊",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            if (showPlatformAd) {
                item {
                    BannerAdView(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        adType = AdType.LEADERBOARD,
                    )
                }
            }

            item {
                Heading(text = "作家データ")
            }
            val authorDataList = bookCardInfo.authorDataList
            authorDataList.forEachIndexed { index, authorData ->
                item {
                    authorData.category?.let {
                        ItemRow(title = "分類：", value = it)
                    }

                    ItemRow(
                        title = "作家名：",
                        value = authorData.authorName,
                        onClick = {
                            onEvent.invoke(BookCardUiEvent.OnClickAuthor(authorData.authorId))
                        },
                    )
                    authorData.authorNameKana?.let {
                        ItemRow(title = "作家名読み：", value = it)
                    }
                    authorData.authorNameRomaji?.let {
                        ItemRow(title = "ローマ字表記：", value = it)
                    }
                    authorData.birth?.let {
                        ItemRow(title = "生年：", value = it)
                    }
                    authorData.death?.let {
                        ItemRow(title = "没年：", value = it)
                    }
                    if (authorData.description != null || authorData.descriptionWikiUrl != null) {
                        Row(
                            modifier =
                                modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 12.dp),
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "人物について：",
                                color = MaterialTheme.colorScheme.inverseSurface,
                            )
                            Column(
                                modifier = Modifier.weight(2f),
                            ) {
                                if (authorData.description != null) {
                                    Text(
                                        text = authorData.description!!,
                                    )
                                }
                                if (authorData.descriptionWikiUrl != null) {
                                    ClickableOrText(
                                        value = "Wikipedia",
                                        onClick = {
                                            Napier.d(tag = TAG) { "onClick: $it" }
                                            uriHandler.openUri(authorData.descriptionWikiUrl!!)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

                if (index != authorDataList.size - 1) {
                    item {
                        HorizontalDivider()
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }

            item {
                Heading(text = "工作員データ")
            }
            val staffData = bookCardInfo.staffData
            staffData?.let {
                item {
                    staffData.input?.let {
                        ItemRow(title = "入力：", value = it)
                    }
                    staffData.proofreading?.let {
                        ItemRow(title = "校正：", value = it)
                    }
                }
            }

            item {
                NavigationBarAnchor()
            }
        }
    }
}

@Composable
private fun NdcCategoryRow(
    modifier: Modifier = Modifier,
    title: String,
    ndcClassificationToDescription: Map<NDCClassification, String>,
    onNdcClick: (NDCClassification) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 12.dp),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            color = MaterialTheme.colorScheme.inverseSurface,
        )

        Column(
            modifier = Modifier.weight(2f),
        ) {
            ndcClassificationToDescription.forEach { (ndcClassification, description) ->
                ClickableOrText(
                    value = description,
                    onClick = {
                        onNdcClick.invoke(ndcClassification)
                    },
                )
            }
        }
    }
}
