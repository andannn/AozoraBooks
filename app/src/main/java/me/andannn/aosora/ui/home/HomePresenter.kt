package me.andannn.aosora.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter

class HomePresenter(
    private val navigator: Navigator
) : Presenter<HomeState> {
    @Composable
    override fun present(): HomeState {
        return HomeState { eventSink ->
            when (eventSink) {
                else -> {}
            }
        }
    }
}

@Stable
data class HomeState(
    val evenSink: (HomeUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface HomeUiEvent {
}
