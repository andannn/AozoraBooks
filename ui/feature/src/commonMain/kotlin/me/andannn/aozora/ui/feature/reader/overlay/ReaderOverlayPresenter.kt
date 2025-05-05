package me.andannn.aozora.ui.feature.reader.overlay

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.feature.dialog.OnJumpTo
import me.andannn.aozora.ui.feature.dialog.ReaderSettingDialogId
import me.andannn.aozora.ui.feature.dialog.TableOfContentsDialogId
import me.andannn.aozora.ui.feature.reader.viewer.BookPageState

private const val TAG = "ReaderOverlayPresenter"

@Composable
fun rememberReaderOverlayPresenter(
    bookPageState: BookPageState,
    popupController: PopupController = LocalPopupController.current,
) = remember(bookPageState, popupController) {
    ReaderOverlayPresenter(bookPageState, popupController)
}

class ReaderOverlayPresenter(
    private val bookPageState: BookPageState,
    private val popupController: PopupController,
) : Presenter<ReaderOverlayState> {
    @Composable
    override fun present(): ReaderOverlayState {
        val scope = rememberCoroutineScope()
        return ReaderOverlayState(bookPageState.pagerState) { event ->
            when (event) {
                ReaderOverlayEvent.OnOpenFontSetting -> {
                    scope.launch {
                        popupController.showDialog(ReaderSettingDialogId)
                    }
                }

                ReaderOverlayEvent.OnOpenTableOfContents -> {
                    scope.launch {
                        val result = popupController.showDialog(TableOfContentsDialogId)
                        Napier.d(tag = TAG) { "on jump to result $result" }
                        if (result is OnJumpTo) {
                            onJumpTo(result.lineNumber)
                        }
                    }
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
    val eventSink: (ReaderOverlayEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderOverlayEvent {
    data object OnOpenFontSetting : ReaderOverlayEvent

    data object OnOpenTableOfContents : ReaderOverlayEvent
}
