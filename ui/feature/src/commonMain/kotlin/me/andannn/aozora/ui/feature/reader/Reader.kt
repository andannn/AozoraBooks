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
import androidx.compose.ui.input.pointer.pointerInput
import me.andannn.aozora.core.domain.model.CachedBookModel
import me.andannn.aozora.core.domain.pagesource.BookPageSource
import me.andannn.aozora.core.domain.pagesource.LocalBookPageSource
import me.andannn.aozora.ui.common.dialog.ActionDialog
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.internal.DefaultDialogController
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlay
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlayEvent
import me.andannn.aozora.ui.feature.reader.overlay.rememberReaderOverlayPresenter
import me.andannn.aozora.ui.feature.reader.viewer.BookViewer
import me.andannn.aozora.ui.feature.reader.viewer.rememberBookViewerPresenter
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun Reader(
    state: ReaderState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val bookCard = state.bookCard
    if (bookCard == null) {
        Scaffold(modifier.fillMaxSize()) {}
        return
    }

    val pageSource =
        remember(bookCard, scope) {
            getKoin().get<BookPageSource.Factory>().createBookPageSource(bookCard, scope)
        }

    CompositionLocalProvider(
        LocalBookPageSource provides pageSource,
        LocalPopupController provides DefaultDialogController(),
    ) {
        ReaderContent(
            bookCard = bookCard,
            modifier = modifier,
        )

        ActionDialog()
    }
}

@Composable
private fun ReaderContent(
    bookCard: CachedBookModel,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val viewerState =
            rememberBookViewerPresenter(
                card = bookCard,
                screenWidthDp = maxWidth,
                screenHeightDp = maxHeight,
            ).present()
        val overlayState =
            rememberReaderOverlayPresenter(bookCard.id, viewerState.bookPageState).present()

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
