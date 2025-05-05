package me.andannn.aozora.ui.feature.reader.overlay

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.feature.dialog.ReaderSettingDialogId

@Composable
fun rememberReaderOverlayPresenter(
    pagerState: PagerState,
    popupController: PopupController = LocalPopupController.current,
) = remember(pagerState, popupController) {
    ReaderOverlayPresenter(pagerState, popupController)
}

class ReaderOverlayPresenter(
    private val pagerState: PagerState,
    private val popupController: PopupController,
) : Presenter<ReaderOverlayState> {
    @Composable
    override fun present(): ReaderOverlayState {
        val scope = rememberCoroutineScope()
        return ReaderOverlayState(pagerState) { event ->
            when (event) {
                ReaderOverlayEvent.OnOpenFontSetting -> {
                    scope.launch {
                        popupController.showDialog(ReaderSettingDialogId)
                    }
                }
            }
        }
    }
}

data class ReaderOverlayState(
    val pagerState: PagerState,
    val eventSink: (ReaderOverlayEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderOverlayEvent {
    data object OnOpenFontSetting : ReaderOverlayEvent
}
