package me.andannn.aozora.ui.feature.reader

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.pagesource.AozoraBookPageSource
import me.andannn.aozora.core.pagesource.LocalBookPageSource
import me.andannn.aozora.ui.common.dialog.ActionDialog
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.internal.DefaultDialogController
import me.andannn.aozora.ui.feature.reader.overlay.ReaderOverlay
import me.andannn.aozora.ui.feature.reader.overlay.rememberReaderOverlayPresenter
import me.andannn.aozora.ui.feature.reader.viewer.BookViewer
import me.andannn.aozora.ui.feature.reader.viewer.rememberBookViewerPresenter

@Composable
fun Reader(
    state: ReaderState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val bookSource =
        remember {
            AozoraBookPageSource(state.bookCard, scope)
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
            bookCard = state.bookCard,
            modifier = modifier,
            onEvent = state.evenSink,
        )

        ActionDialog()
    }
}

@Composable
private fun ReaderContent(
    bookCard: AozoraBookCard,
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
        val overlayState = rememberReaderOverlayPresenter(viewerState.pagerState).present()
        BookViewer(
            state = viewerState,
        )
        ReaderOverlay(
            state = overlayState,
        )
    }
}
