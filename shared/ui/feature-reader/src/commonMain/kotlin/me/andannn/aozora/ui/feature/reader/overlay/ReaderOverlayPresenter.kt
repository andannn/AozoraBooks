/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import io.github.aakira.napier.Napier
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.retainPresenter
import me.andannn.aozora.ui.common.util.SystemUiVisibilityEffect

private const val TAG = "ReaderOverlayPresenter"

@Composable
internal fun retainReaderOverlayPresenter() =
    retainPresenter {
        ReaderOverlayPresenter()
    }

private class ReaderOverlayPresenter : RetainedPresenter<ReaderOverlayState>() {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun present(): ReaderOverlayState {
        var showOverlay by rememberSaveable {
            mutableStateOf(false)
        }
        SystemUiVisibilityEffect(visible = showOverlay)

        return ReaderOverlayState(
            showOverlay = showOverlay,
        ) { event ->
            Napier.d(tag = TAG) { "on event $event" }
            when (event) {
                ReaderOverlayEvent.OnCloseOverlay -> {
                    showOverlay = false
                }

                ReaderOverlayEvent.OnToggleOverlay -> {
                    showOverlay = !showOverlay
                }
            }
        }
    }
}

data class ReaderOverlayState(
    val showOverlay: Boolean,
    val eventSink: (ReaderOverlayEvent) -> Unit = {},
)

sealed interface ReaderOverlayEvent {
    data object OnCloseOverlay : ReaderOverlayEvent

    data object OnToggleOverlay : ReaderOverlayEvent
}
