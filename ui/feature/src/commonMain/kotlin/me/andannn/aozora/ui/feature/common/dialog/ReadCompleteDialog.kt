/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.common.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.andannn.aozora.ui.common.dialog.DialogAction
import me.andannn.aozora.ui.common.dialog.DialogId
import me.andannn.aozora.ui.common.dialog.DialogType
import me.andannn.aozora.ui.common.dialog.Dismissed
import me.andannn.platform.Platform
import me.andannn.platform.platform

object ReaderCompleteDialogId : DialogId {
    override val dialogType: DialogType = DialogType.AlertDialog

    @Composable
    override fun Content(onAction: (DialogAction) -> Unit) {
        ReaderCompletedDialog(onAction = onAction)
    }
}

object OnGoToAppStore : DialogAction

@Composable
private fun ReaderCompletedDialog(
    modifier: Modifier = Modifier,
    onAction: (DialogAction) -> Unit,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "読了おめでとうございます！",
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "この本は読了済みとして記録されました。\n\nよろしければ、${platform.getStoreString()} での評価やご感想をお聞かせください！",
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Only support android now.
        if (platform == Platform.ANDROID) {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = {
                        onAction(Dismissed)
                    },
                ) {
                    Text("後で")
                }

                TextButton(
                    onClick = {
                        onAction(OnGoToAppStore)
                    },
                ) {
                    Text("アプリを評価する")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

private fun Platform.getStoreString() =
    when (this) {
        Platform.IOS -> "App Store"
        Platform.ANDROID -> "Google Play"
    }
