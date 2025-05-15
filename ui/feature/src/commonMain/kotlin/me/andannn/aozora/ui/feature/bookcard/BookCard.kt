/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.bookcard

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.ui.common.widgets.AdType
import me.andannn.aozora.ui.common.widgets.BannerAdView

private const val TAG = "BookCard"

@Composable
fun BookCard(
    state: BookCardState,
    modifier: Modifier = Modifier,
) {
    BookCardContent(
        modifier = modifier,
        bookCardInfo = state.bookCardInfo,
        isAddedToShelf = state.isAddedToShelf,
        onEvent = state.evenSink,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookCardContent(
    modifier: Modifier = Modifier,
    bookCardInfo: AozoraBookCard?,
    isAddedToShelf: Boolean,
    onEvent: (BookCardUiEvent) -> Unit = {},
) {
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
                    Text("図書カード：No.${bookCardInfo.id}")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(BookCardUiEvent.Back)
                        },
                    ) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
                    }
                },
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                SmallFloatingActionButton(
                    content = {
                        Text("読む")
                    },
                    onClick = {
                        onEvent.invoke(BookCardUiEvent.OnClickRead)
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExtendedFloatingActionButton(
                    content = {
                        if (isAddedToShelf) {
                            Text("本棚から削除")
                        } else {
                            Text("本棚に追加")
                        }
                    },
                    onClick = {
                        onEvent.invoke(BookCardUiEvent.OnAddToShelf)
                    },
                )
            }
        },
    ) {
        LazyColumn(
            Modifier.padding(it),
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center).width(IntrinsicSize.Max),
                    ) {
                        Spacer(Modifier.height(24.dp))
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
                                bookCardInfo.authorUrl?.let {
                                    Napier.d(tag = TAG) { "onClick: $it" }
                                }
                            },
                        )
                    }
                }
            }

            item {
                Heading(text = "作品データ")
            }

            item {
                bookCardInfo.category?.let {
                    ItemRow(title = "分類：", value = it)
                }
                bookCardInfo.source?.let {
                    ItemRow(title = "初出：", value = it)
                }
                bookCardInfo.characterType?.let {
                    ItemRow(title = "文字遣い種別：", value = it)
                }
            }

            item {
                BannerAdView(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    adType = AdType.LEADERBOARD,
                )
            }

            item {
                Heading(text = "作家データ")
            }
            val authorDataList = bookCardInfo.authorDataList
            authorDataList.forEachIndexed { index, authorData ->
                item {
                    ItemRow(title = "分類：", value = authorData.category)

                    ItemRow(
                        title = "作家名：",
                        value = authorData.authorName,
                        onClick =
                            authorData.authorUrl?.let {
                                {
// TODO:
                                    Napier.d(tag = TAG) { "onClick: $it" }
                                }
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
        }
    }
}

@Composable
private fun Heading(
    modifier: Modifier = Modifier,
    text: String,
) {
    Column(
        modifier = modifier.padding(vertical = 12.dp),
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp).padding(start = 12.dp),
                text = text,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@Composable
private fun ItemRow(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 12.dp),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            color = MaterialTheme.colorScheme.inverseSurface,
        )
        ClickableOrText(
            modifier = Modifier.weight(2f),
            value = value,
            onClick = onClick,
        )
    }
}

@Composable
private fun ClickableOrText(
    modifier: Modifier = Modifier,
    value: String,
    onClick: (() -> Unit)? = null,
) {
    Box(modifier) {
        if (onClick != null) {
            val annotatedString =
                buildAnnotatedString {
                    withStyle(
                        style =
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline,
                            ),
                    ) {
                        append(value)
                    }
                }
            Text(
                modifier =
                    Modifier.clickable(
                        onClick = onClick,
                        indication = null,
                        interactionSource = null,
                    ),
                text = annotatedString,
            )
        } else {
            Text(
                text = value,
            )
        }
    }
}
