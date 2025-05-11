/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.dialog

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

enum class DialogType {
    AlertDialog,
    ModalBottomSheet,
}

@Composable
fun ActionDialog(
    popupController: PopupController = LocalPopupController.current,
    data: DialogData? = popupController.currentDialog,
) {
    val dataState = rememberUpdatedState(data)
    if (dataState.value != null) {
        ActionDialogContainer(
            data = dataState.value!!,
            dialogContent = {
                it.dialogId.Content(
                    onAction = { action ->
                        it.performAction(action)
                    },
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionDialogContainer(
    data: DialogData,
    dialogContent: @Composable (DialogData) -> Unit,
) {
    when (data.dialogId.dialogType) {
        DialogType.AlertDialog ->
            Dialog(
                onDismissRequest = {
                    data.performAction(Dismissed)
                },
                content = {
                    Surface(
                        modifier = Modifier.wrapContentSize(),
                        shape = AlertDialogDefaults.shape,
                        tonalElevation = AlertDialogDefaults.TonalElevation,
                    ) {
                        dialogContent(data)
                    }
                },
            )

        DialogType.ModalBottomSheet ->
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                onDismissRequest = {
                    data.performAction(Dismissed)
                },
                content = {
                    dialogContent(data)
                },
            )
    }
}
