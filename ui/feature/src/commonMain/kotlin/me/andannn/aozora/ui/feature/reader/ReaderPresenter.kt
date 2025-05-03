package me.andannn.aozora.ui.feature.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.feature.settings.ReaderSettingDialogId

@Composable
fun rememberReaderPresenter(
    cardId: String,
    popupController: PopupController = LocalPopupController.current,
) = remember(
    cardId,
    popupController,
) {
    ReaderPresenter(cardId, popupController)
}

class ReaderPresenter(
    private val cardId: String,
    private val popupController: PopupController,
) : Presenter<ReaderState> {
    @Composable
    override fun present(): ReaderState {
        print(cardId)
        val scope = rememberCoroutineScope()
        return ReaderState(cardId) { event ->
            when (event) {
                ReaderUiEvent.OnOpenFontSetting -> {
                    scope.launch {
                        popupController.showDialog(ReaderSettingDialogId)
                    }
                }
            }
        }
    }
}

@Stable
data class ReaderState(
    val cardId: String,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent {
    data object OnOpenFontSetting : ReaderUiEvent
}
