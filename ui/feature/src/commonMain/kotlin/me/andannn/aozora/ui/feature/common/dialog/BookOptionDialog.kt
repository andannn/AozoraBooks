/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.common.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.andannn.aozora.ui.common.dialog.DialogAction
import me.andannn.aozora.ui.common.dialog.DialogId
import me.andannn.aozora.ui.common.dialog.DialogType
import me.andannn.aozora.ui.common.dialog.PopupController

suspend fun PopupController.showBookOptionDialog(fromReadingTab: Boolean): DialogAction {
    val options =
        if (fromReadingTab) {
            listOf(
                OptionItem.OPEN_BOOK_CARD,
                OptionItem.REMOVE_FROM_BOOK_SHELF,
                OptionItem.MARK_AS_COMPLETED,
            )
        } else {
            listOf(
                OptionItem.OPEN_BOOK_CARD,
                OptionItem.REMOVE_FROM_BOOK_SHELF,
                OptionItem.MARK_AS_NOT_COMPLETED,
            )
        }
    return showDialog(BookOptionDialogId(options))
}

data class BookOptionDialogId(
    val options: List<OptionItem>,
) : DialogId {
    override val dialogType: DialogType = DialogType.ModalBottomSheet

    @Composable
    override fun Content(onAction: (DialogAction) -> Unit) {
        BookOptionDialog(options = options, onAction = onAction)
    }
}

data class OnClickOption(
    val option: OptionItem,
) : DialogAction

@Composable
private fun BookOptionDialog(
    modifier: Modifier = Modifier,
    options: List<OptionItem>,
    onAction: (DialogAction) -> Unit = {},
) {
    Column(modifier.navigationBarsPadding().fillMaxWidth()) {
        options.forEach {
            SheetItem(
                icon = it.icon(),
                label = it.label(),
                onClick = {
                    onAction(OnClickOption(it))
                },
            )
        }
    }
}

enum class OptionItem {
    OPEN_BOOK_CARD,
    REMOVE_FROM_BOOK_SHELF,
    MARK_AS_COMPLETED,
    MARK_AS_NOT_COMPLETED,
}

fun OptionItem.label() =
    when (this) {
        OptionItem.OPEN_BOOK_CARD -> "図書カードを開く"
        OptionItem.REMOVE_FROM_BOOK_SHELF -> "本棚から削除"
        OptionItem.MARK_AS_COMPLETED -> "読了にする"
        OptionItem.MARK_AS_NOT_COMPLETED -> "読了にしない"
    }

fun OptionItem.icon() =
    when (this) {
        OptionItem.OPEN_BOOK_CARD -> Icons.Filled.Bookmark
        OptionItem.REMOVE_FROM_BOOK_SHELF -> Icons.Filled.Delete
        OptionItem.MARK_AS_COMPLETED -> Icons.Filled.BookmarkAdd
        OptionItem.MARK_AS_NOT_COMPLETED -> Icons.Filled.BookmarkRemove
    }

@Composable
internal fun SheetItem(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
