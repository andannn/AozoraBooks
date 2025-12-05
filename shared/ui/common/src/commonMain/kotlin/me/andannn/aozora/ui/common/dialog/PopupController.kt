/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import me.andannn.aozora.ui.common.dialog.internal.DefaultDialogController

val LocalPopupController: ProvidableCompositionLocal<PopupController> =
    compositionLocalOf { error("no popup controller") }

@Suppress("ktlint:standard:function-naming")
fun PopupController(): PopupController = DefaultDialogController()

interface DialogData {
    val dialogId: DialogId

    fun performAction(action: DialogAction)
}

/**
 * Dialog controller
 */
interface PopupController {
    /**
     * current dialog
     */
    val currentDialog: DialogData?

    suspend fun showDialog(dialogId: DialogId): DialogAction
}

/**
 * user interaction
 */
interface DialogAction

data object Dismissed : DialogAction

/**
 * Dialog id
 */
interface DialogId {
    /**
     * Dialog type
     */
    val dialogType: DialogType

    /**
     * Dialog content
     */
    @Composable fun Content(onAction: (DialogAction) -> Unit)
}
