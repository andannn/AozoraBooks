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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.pagesource.LocalBookPageSource
import me.andannn.aozora.ui.common.Presenter
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlay
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlayEvent
import me.andannn.aozora.ui.feature.reader.overlay.retainReaderOverlayPresenter
import me.andannn.aozora.ui.feature.reader.viewer.BookViewer
import me.andannn.aozora.ui.feature.reader.viewer.BookViewerUiEvent
import me.andannn.aozora.ui.feature.reader.viewer.retainBookViewerPresenter

@Composable
fun Reader(
    cardId: String,
    authorId: String,
    presenter: Presenter<ReaderState> =
        retainReaderPresenter(
            cardId = cardId,
            authorId = authorId,
        ),
    modifier: Modifier = Modifier,
) {
    Reader(
        state = presenter.present(),
        modifier = modifier,
    )
}

@Composable
private fun Reader(
    state: ReaderState,
    modifier: Modifier = Modifier,
) {
    val bookCard = state.bookCard
    val bookPageSource = state.bookPageSource
    if (bookCard == null || bookPageSource == null) {
        Scaffold(modifier.fillMaxSize()) {}
        return
    }

    CompositionLocalProvider(
        LocalBookPageSource provides state.bookPageSource,
    ) {
        ReaderContent(
            bookCard = bookCard,
            modifier = modifier,
        )
    }
}

@Composable
private fun ReaderContent(
    bookCard: AozoraBookCard,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val viewerState =
            retainBookViewerPresenter(
                card = bookCard,
                screenWidthDp = maxWidth,
                screenHeightDp = maxHeight,
            ).present()
        val overlayState =
            retainReaderOverlayPresenter(
                bookCard.id,
                bookCard.authorId,
                viewerState.bookPageState,
            ).present()

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
                                    if (!isConsumedByPager && !changeEvent.isConsumed) {
                                        overlayState.eventSink.invoke(ReaderOverlayEvent.OnToggleOverlay)
                                        changeEvent.consume()
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
                onRequestShowSettingDialog = {
                    viewerState.evenSink.invoke(BookViewerUiEvent.OnShowSettingDialog)
                },
                onRequestShowTableOfContentDialog = {
                    viewerState.evenSink.invoke(BookViewerUiEvent.OnShowTableOfContentDialog)
                },
            )
        }
    }
}
