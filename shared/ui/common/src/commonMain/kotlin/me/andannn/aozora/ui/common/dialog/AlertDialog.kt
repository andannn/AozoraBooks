/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.dialog

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
import aosora.shared.ui.common.generated.resources.Res
import aosora.shared.ui.common.generated.resources.copyright_retained_msg
import aosora.shared.ui.common.generated.resources.copyright_retained_title
import aosora.shared.ui.common.generated.resources.download_book_error
import aosora.shared.ui.common.generated.resources.ok
import aosora.shared.ui.common.generated.resources.open_by_browser
import aosora.shared.ui.common.generated.resources.unknown_error
import me.andannn.aozora.core.domain.exceptions.CopyRightRetainedException
import me.andannn.aozora.core.domain.exceptions.DownloadBookFailedException
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

suspend fun PopupController.showAlertDialog(throwable: Throwable): DialogAction =
    when (throwable) {
        is CopyRightRetainedException -> showDialog(CopyRightRetainedDialog)
        is DownloadBookFailedException -> showDialog(DownloadBookErrorDialog)
        else -> showDialog(UnKnownErrorDialog)
    }

abstract class AlertDialog(
    val title: StringResource? = null,
    val message: StringResource? = null,
    val positive: StringResource,
    val negative: StringResource? = null,
) : DialogId {
    override val dialogType: DialogType = DialogType.AlertDialog

    @Composable
    override fun Content(onAction: (DialogAction) -> Unit) {
        AlertDialog(dialogId = this, onAction = onAction)
    }
}

data object OnAccept : DialogAction

data object OnDecline : DialogAction

@Composable
private fun AlertDialog(
    modifier: Modifier = Modifier,
    dialogId: AlertDialog,
    onAction: (DialogAction) -> Unit,
) {
    Column(modifier = modifier.padding(16.dp)) {
        dialogId.title?.let {
            Text(
                text = stringResource(dialogId.title),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        dialogId.message?.let {
            Text(
                text = stringResource(dialogId.message),
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Row {
            Spacer(modifier = Modifier.weight(1f))
            dialogId.negative?.let {
                TextButton(
                    onClick = {
                        onAction(OnDecline)
                    },
                ) {
                    Text(stringResource(dialogId.negative))
                }
            }

            TextButton(
                onClick = {
                    onAction(OnAccept)
                },
            ) {
                Text(stringResource(dialogId.positive))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

data object DownloadBookErrorDialog : AlertDialog(
    message = Res.string.download_book_error,
    positive = Res.string.ok,
)

data object CopyRightRetainedDialog : AlertDialog(
    title = Res.string.copyright_retained_title,
    message = Res.string.copyright_retained_msg,
    positive = Res.string.open_by_browser,
)

data object UnKnownErrorDialog : AlertDialog(
    message = Res.string.unknown_error,
    positive = Res.string.ok,
)
