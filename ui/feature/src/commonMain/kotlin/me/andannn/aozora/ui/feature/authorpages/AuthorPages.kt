/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.authorpages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.ui.common.widgets.AuthorColumnItemView
import me.andannn.aozora.ui.common.widgets.BannerAdView
import me.andannn.platform.AdType
import me.andannn.platform.showPlatformAd

@Composable
fun AuthorPages(
    state: AuthorPagesState,
    modifier: Modifier = Modifier,
) {
    AuthorPagesContent(
        modifier = modifier,
        kanaLineLabel = state.kanaLineItem.kanaLabel,
        pagingData = state.pagingData,
        onEvent = state.evenSink,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorPagesContent(
    modifier: Modifier,
    kanaLineLabel: String,
    pagingData: LazyPagingItems<AuthorData>,
    onEvent: (AuthorPagesUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("作家リスト：$kanaLineLabel")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent.invoke(AuthorPagesUiEvent.OnBack)
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
                        AuthorColumnItemView(
                            modifier = Modifier.fillMaxWidth(),
                            index = index,
                            item = it,
                            onClick = {
                                onEvent.invoke(AuthorPagesUiEvent.OnAuthorClick(it))
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
        }
    }
}
