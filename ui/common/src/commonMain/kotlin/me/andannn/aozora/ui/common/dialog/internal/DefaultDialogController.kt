/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.dialog.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.andannn.aozora.ui.common.dialog.DialogAction
import me.andannn.aozora.ui.common.dialog.DialogData
import me.andannn.aozora.ui.common.dialog.DialogId
import me.andannn.aozora.ui.common.dialog.PopupController
import kotlin.coroutines.resume

private const val TAG = "PopupController"

class DefaultDialogController : PopupController {
    private val mutex = Mutex()

    private var _currentDialog by mutableStateOf<DialogData?>(null)

    override val currentDialog: DialogData?
        get() = _currentDialog

    /**
     * Show dialog and wait for user interaction.
     *
     * Dialog show at most one snackbar at a time.
     */
    override suspend fun showDialog(dialogId: DialogId): DialogAction =
        mutex.withLock {
            Napier.d(tag = TAG) { "show dialog. dialogId = $dialogId" }
            try {
                return suspendCancellableCoroutine { continuation ->
                    _currentDialog = DialogDataImpl(dialogId, continuation)
                }
            } finally {
                Napier.d(tag = TAG) { "currentDialog closed = $dialogId" }
                _currentDialog = null
            }
        }
}

class DialogDataImpl(
    override val dialogId: DialogId,
    private val continuation: CancellableContinuation<DialogAction>,
) : DialogData {
    override fun performAction(action: DialogAction) {
        if (continuation.isActive) continuation.resume(action)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DialogDataImpl

        if (dialogId != other.dialogId) return false
        if (continuation != other.continuation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dialogId.hashCode()
        result = 31 * result + continuation.hashCode()
        return result
    }
}
