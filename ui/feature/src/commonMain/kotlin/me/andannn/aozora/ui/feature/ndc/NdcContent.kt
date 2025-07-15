/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.ndc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.NdcData
import me.andannn.aozora.ui.common.widgets.NavigationBarAnchor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NdcContent(
    state: NdcContentState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(state.title)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            state.evenSink.invoke(NdcContentUiEvent.Back)
                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (state) {
                is NdcContentState.NdcDetails -> {
                    NdcDetail(
                        modifier = modifier,
                        eventSink = state.evenSink,
                    )
                }

                is NdcContentState.NdcChildren -> {
                    NdcChildren(
                        modifier = modifier,
                        ndcChildren = state.children,
                        eventSink = state.evenSink,
                    )
                }
            }
        }
    }
}

@Composable
private fun NdcChildren(
    modifier: Modifier = Modifier,
    ndcChildren: List<NdcData>,
    eventSink: (NdcContentUiEvent) -> Unit,
) {
    LazyColumn(modifier) {
        items(
            items = ndcChildren,
            key = { it.ndcClassification.value },
        ) { item ->
            NdcCategoryItem(
                ndcData = item,
                onClick = {
                    eventSink(NdcContentUiEvent.OnNdcItemClick(item.ndcClassification))
                },
            )
        }

        item {
            NavigationBarAnchor()
        }
    }
}

@Composable
private fun NdcDetail(
    modifier: Modifier = Modifier,
    eventSink: (NdcContentUiEvent) -> Unit,
) {
}

@Composable
private fun NdcCategoryItem(
    ndcData: NdcData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                ndcData.ndcClassification.toString(),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                ndcData.label,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
