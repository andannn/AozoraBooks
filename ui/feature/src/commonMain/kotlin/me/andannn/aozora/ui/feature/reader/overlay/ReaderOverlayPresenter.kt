package me.andannn.aozora.ui.feature.reader.overlay

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.dialog.OnJumpTo
import me.andannn.aozora.ui.feature.dialog.ReaderSettingDialogId
import me.andannn.aozora.ui.feature.dialog.TableOfContentsDialogId
import me.andannn.aozora.ui.feature.reader.viewer.BookPageState

private const val TAG = "ReaderOverlayPresenter"

@Composable
fun rememberReaderOverlayPresenter(
    bookPageState: BookPageState,
    navigator: Navigator = LocalNavigator.current,
    popupController: PopupController = LocalPopupController.current,
) = remember(bookPageState, popupController, navigator) {
    ReaderOverlayPresenter(navigator, bookPageState, popupController)
}

class ReaderOverlayPresenter(
    private val navigator: Navigator,
    private val bookPageState: BookPageState,
    private val popupController: PopupController,
) : Presenter<ReaderOverlayState> {
    @Composable
    override fun present(): ReaderOverlayState {
        val scope = rememberCoroutineScope()
        var showOverlay by remember {
            mutableStateOf(false)
        }
        return ReaderOverlayState(
            pagerState = bookPageState.pagerState,
            showOverlay = showOverlay,
        ) { event ->
            Napier.d(tag = TAG) { "on event $event" }
            when (event) {
                ReaderOverlayEvent.OnOpenFontSetting -> {
                    showOverlay = false
                    scope.launch {
                        popupController.showDialog(ReaderSettingDialogId)
                    }
                }

                ReaderOverlayEvent.OnOpenTableOfContents -> {
                    showOverlay = false
                    scope.launch {
                        val result = popupController.showDialog(TableOfContentsDialogId)
                        Napier.d(tag = TAG) { "on jump to result $result" }
                        if (result is OnJumpTo) {
                            onJumpTo(result.lineNumber)
                        }
                    }
                }

                ReaderOverlayEvent.OnToggleOverlay -> {
                    showOverlay = !showOverlay
                }

                ReaderOverlayEvent.OnBack -> {
                    navigator.pop()
                }
            }
        }
    }

    private suspend fun onJumpTo(lineNumber: Int) {
        val pageIndex =
            bookPageState.pages.indexOfFirst { page ->
                when (page) {
                    is AozoraPage.AozoraBibliographicalPage -> {
// TODO
                        false
                    }

                    is AozoraPage.AozoraCoverPage -> {
// TODO
                        false
                    }

                    is AozoraPage.AozoraRoughPage -> {
                        lineNumber in page.pageProgress
                    }
                }
            }
        if (pageIndex != -1) {
            bookPageState.pagerState.animateScrollToPage(pageIndex)
        }
    }
}

data class ReaderOverlayState(
    val pagerState: PagerState,
    val showOverlay: Boolean,
    val eventSink: (ReaderOverlayEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderOverlayEvent {
    data object OnOpenFontSetting : ReaderOverlayEvent

    data object OnOpenTableOfContents : ReaderOverlayEvent

    data object OnToggleOverlay : ReaderOverlayEvent

    data object OnBack : ReaderOverlayEvent
}
