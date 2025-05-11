/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import me.andannn.aozora.core.data.common.BookModelTemp
import me.andannn.aozora.core.pagesource.AozoraBookPageSource
import me.andannn.aozora.core.pagesource.LocalBookPageSource
import me.andannn.aozora.ui.common.dialog.ActionDialog
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.internal.DefaultDialogController
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlay
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlayEvent
import me.andannn.aozora.ui.feature.reader.overlay.rememberReaderOverlayPresenter
import me.andannn.aozora.ui.feature.reader.viewer.BookViewer
import me.andannn.aozora.ui.feature.reader.viewer.rememberBookViewerPresenter

@Composable
fun Reader(
    state: ReaderState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val savedBook = state.bookCard
    if (savedBook == null) {
        Scaffold(modifier.fillMaxSize()) {}
        return
    }

    val bookSource =
        remember {
            AozoraBookPageSource(savedBook, scope)
        }
    val popupController =
        remember {
            DefaultDialogController()
        }
    CompositionLocalProvider(
        LocalBookPageSource provides bookSource,
        LocalPopupController provides popupController,
    ) {
        ReaderContent(
            bookCard = savedBook,
            modifier = modifier,
            onEvent = state.evenSink,
        )

        ActionDialog()
    }
}

@Composable
private fun ReaderContent(
    bookCard: BookModelTemp,
    modifier: Modifier = Modifier,
    onEvent: (ReaderUiEvent) -> Unit,
) {
    val localDensity = LocalDensity.current
    BoxWithConstraints(modifier = modifier) {
        val maxHeight = with(localDensity) { this@BoxWithConstraints.maxHeight.toPx() }
        val maxWidth = with(localDensity) { maxWidth.toPx() }

        val viewerState =
            rememberBookViewerPresenter(
                card = bookCard,
                screenSize = Size(maxWidth, maxHeight),
            ).present()
        val overlayState = rememberReaderOverlayPresenter(viewerState.bookPageState).present()

        Box {
            BookViewer(
                modifier =
                    Modifier.fillMaxSize().pointerInput(Unit) {
                        awaitPointerEventScope {
                            var isConsumedByPager = false
                            while (true) {
                                val event = awaitPointerEvent()
                                val changeEvent = event.changes.first()
                                if (changeEvent.pressed && changeEvent.isConsumed) {
                                    isConsumedByPager = true
                                }
                                if (!changeEvent.pressed) {
                                    if (!isConsumedByPager) {
                                        overlayState.eventSink.invoke(ReaderOverlayEvent.OnToggleOverlay)
                                    }
                                    isConsumedByPager = false
                                }
                            }
                        }
                    },
                state = viewerState,
            )

            ReaderOverlay(
                state = overlayState,
            )
        }
    }
}
