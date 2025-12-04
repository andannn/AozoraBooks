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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.pagesource.BookPageSource
import me.andannn.aozora.core.domain.pagesource.LocalBookPageSource
import me.andannn.aozora.ui.common.Presenter
import me.andannn.aozora.ui.common.dialog.ActionDialog
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.internal.DefaultDialogController
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlay
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlayEvent
import me.andannn.aozora.ui.feature.reader.overlay.retainReaderOverlayPresenter
import me.andannn.aozora.ui.feature.reader.viewer.BookViewer
import me.andannn.aozora.ui.feature.reader.viewer.retainBookViewerPresenter
import org.koin.mp.KoinPlatform.getKoin

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

    DisposableEffect(Unit) {
        onDispose {
            pageSource.close()
        }
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
            )
        }
    }
}
