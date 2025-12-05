/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.TableOfContentsModel

data class TableOfContentsDialogId(
    val tableOfContentList: List<TableOfContentsModel>,
) : DialogId {
    override val dialogType: DialogType = DialogType.ModalBottomSheet

    @Composable
    override fun Content(onAction: (DialogAction) -> Unit) {
        TableOfContentsDialog(
            tableOfContentList,
            onAction,
        )
    }
}

data class TableOfContentsState(
    val tableOfContentsList: List<TableOfContentsModel> = emptyList(),
)

data class OnJumpTo(
    val blockIndex: Int,
) : DialogAction

@Composable
fun TableOfContentsDialog(
    tableOfContentList: List<TableOfContentsModel>,
    onAction: (DialogAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    TableOfContentsDialogContent(
        modifier = modifier,
        state = TableOfContentsState(tableOfContentList),
        onClickTableOfContent = {
            onAction(OnJumpTo(it.blockIndex))
        },
    )
}

@Composable
fun TableOfContentsDialogContent(
    state: TableOfContentsState,
    modifier: Modifier = Modifier,
    onClickTableOfContent: (TableOfContentsModel) -> Unit = {},
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Column {
                Text(
                    modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp),
                    text = "目次",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        val maxHeadingLevel = state.tableOfContentsList.minOfOrNull { it.headingLevel }

        items(
            items = state.tableOfContentsList,
            key = { "${it.title}_${it.headingLevel}_${it.blockIndex}" },
        ) { tableOfContent ->
            TableOfContentItem(
                modifier = modifier.fillMaxWidth(),
                spacerCount = maxHeadingLevel?.let { tableOfContent.headingLevel - it },
                tableOfContent = tableOfContent,
                onClick = {
                    onClickTableOfContent(tableOfContent)
                },
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TableOfContentItem(
    modifier: Modifier = Modifier,
    tableOfContent: TableOfContentsModel,
    onClick: () -> Unit = {},
    spacerCount: Int?,
) {
    Row(modifier.clickable(onClick = onClick).padding(vertical = 12.dp, horizontal = 16.dp)) {
        if (spacerCount != null) {
            repeat(spacerCount) {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
        Text(
            tableOfContent.title,
            style = getHeadingLevel(tableOfContent.headingLevel),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun getHeadingLevel(headingLevel: Int): TextStyle =
    when (headingLevel) {
        1 -> MaterialTheme.typography.headlineLarge
        2 -> MaterialTheme.typography.headlineMedium
        3 -> MaterialTheme.typography.headlineSmall
        4 -> MaterialTheme.typography.bodyLarge
        5 -> MaterialTheme.typography.bodyMedium
        6 -> MaterialTheme.typography.bodySmall
        else -> MaterialTheme.typography.bodySmall
    }
