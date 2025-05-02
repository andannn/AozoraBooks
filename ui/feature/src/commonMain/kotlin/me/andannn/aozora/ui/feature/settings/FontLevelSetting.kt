package me.andannn.aozora.ui.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.ui.common.dialog.DialogAction
import me.andannn.aozora.ui.common.dialog.DialogId
import me.andannn.aozora.ui.common.dialog.DialogType

data class FontLevelSettingDialogId(
    override val dialogType: DialogType = DialogType.AlertDialog,
) : DialogId {
    @Composable
    override fun Content(onAction: (DialogAction) -> Unit) {
        FontLevelSettingDialogContent(onAction)
    }
}

@Composable
fun rememberFontLevelSettingDialogPresenter() =
    remember {
        FontLevelSettingDialogPresenter()
    }

class FontLevelSettingDialogPresenter : Presenter<FontLevelState> {
    @Composable
    override fun present(): FontLevelState = FontLevelState()
}

@Stable
data class FontLevelState(
    val eventSink: (FontLevelUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface FontLevelUiEvent

@Composable
private fun FontLevelSettingDialogContent(onAction: (DialogAction) -> Unit) {
    val state = rememberFontLevelSettingDialogPresenter().present()
}
