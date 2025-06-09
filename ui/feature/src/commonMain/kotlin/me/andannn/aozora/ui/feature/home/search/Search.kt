/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.ui.common.widgets.BannerAdView
import me.andannn.aozora.ui.common.widgets.NavigationBarAnchor
import me.andannn.platform.AdType
import me.andannn.platform.showPlatformAd

@Composable
fun Search(
    state: SearchState,
    modifier: Modifier = Modifier,
) {
    SearchContent(
        modifier = modifier,
        onEvent = state.evenSink,
    )
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    onEvent: (SearchUiEvent) -> Unit,
) {
    Column(modifier.systemBarsPadding()) {
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier =
                Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = CircleShape,
            onClick = {
                onEvent.invoke(SearchUiEvent.OnSearchBarClick)
            },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Outlined.Search, contentDescription = null)
                Spacer(Modifier.width(16.dp))
                Text(text = "作家・作品を検索", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            columns = GridCells.Adaptive(minSize = 320.dp),
        ) {
            item {
                Column {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = "作品別",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    SearchByKanaArea(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        onKanaItemClicked = {
                            onEvent.invoke(SearchUiEvent.OnClickKanaItem(it))
                        },
                    )
                }
            }

            item {
                Column {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = "作家別",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    SearchByAuthorArea(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        onKanaLineItemClicked = {
                            onEvent.invoke(SearchUiEvent.OnClickKanaLineItem(it))
                        },
                    )
                }
            }

            if (showPlatformAd) {
                item {
                    BannerAdView(
                        modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                        adType = AdType.MEDIUM_RECTANGLE,
                    )
                }
            }

            item {
                NavigationBarAnchor()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchByAuthorArea(
    modifier: Modifier = Modifier,
    onKanaLineItemClicked: (KanaLineItem) -> Unit = {},
) {
    FlowRow(modifier) {
        KanaLineItem.entries.forEach { item ->
            SearchKanaItem(
                modifier = Modifier.size(48.dp),
                kana = item.kanaLabel,
                onClick = {
                    onKanaLineItemClicked.invoke(item)
                },
            )
        }
    }
}

@Composable
private fun SearchByKanaArea(
    modifier: Modifier = Modifier,
    onKanaItemClicked: (KanaItem) -> Unit,
) {
    Column(modifier) {
        KanaItemList.chunked(10).forEach { lineItems ->
            Row(Modifier.fillMaxWidth()) {
                lineItems.forEach { item ->
                    if (item != null) {
                        SearchKanaItem(
                            modifier = Modifier.aspectRatio(1f).weight(1f),
                            kana = item.kanaLabel,
                            onClick = {
                                onKanaItemClicked.invoke(item)
                            },
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchKanaItem(
    modifier: Modifier = Modifier,
    kana: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.padding(2.dp),
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Box(Modifier) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = kana,
            )
        }
    }
}

data class KanaItem(
    val kana: String,
    val kanaLabel: String,
)

private val KanaItemList =
    listOf(
        KanaItem("a", "あ"),
        KanaItem("ka", "か"),
        KanaItem("sa", "さ"),
        KanaItem("ta", "た"),
        KanaItem("na", "な"),
        KanaItem("ha", "は"),
        KanaItem("ma", "ま"),
        KanaItem("ya", "や"),
        KanaItem("ra", "ら"),
        KanaItem("wa", "わ"),
        KanaItem("i", "い"),
        KanaItem("ki", "き"),
        KanaItem("si", "し"),
        KanaItem("ti", "ち"),
        KanaItem("ni", "に"),
        KanaItem("hi", "ひ"),
        KanaItem("mi", "み"),
        null,
        KanaItem("ri", "り"),
        KanaItem("wo", "を"),
        KanaItem("u", "う"),
        KanaItem("ku", "く"),
        KanaItem("su", "す"),
        KanaItem("tu", "つ"),
        KanaItem("nu", "ぬ"),
        KanaItem("hu", "ふ"),
        KanaItem("mu", "む"),
        KanaItem("yu", "ゆ"),
        KanaItem("ru", "る"),
        null,
        KanaItem("e", "え"),
        KanaItem("ke", "け"),
        KanaItem("se", "せ"),
        KanaItem("te", "て"),
        KanaItem("ne", "ね"),
        KanaItem("he", "へ"),
        KanaItem("me", "め"),
        null,
        KanaItem("re", "れ"),
        null,
        KanaItem("o", "お"),
        KanaItem("ko", "こ"),
        KanaItem("so", "そ"),
        KanaItem("to", "と"),
        KanaItem("no", "の"),
        KanaItem("ho", "ほ"),
        KanaItem("mo", "も"),
        KanaItem("yo", "よ"),
        KanaItem("ro", "ろ"),
        null,
    )
