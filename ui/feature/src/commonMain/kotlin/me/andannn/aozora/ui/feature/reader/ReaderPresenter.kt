package me.andannn.aozora.ui.feature.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter

class ReaderPresenter(private val cardId: String) : Presenter<ReaderState> {
    @Composable
    override fun present(): ReaderState {
        return ReaderState(cardId)
    }
}

@Stable
data class ReaderState(
    val cardId: String,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent
