package me.andannn.aozora.ui.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.ui.feature.screens.ReaderScreen

class HomePresenter(
    private val navigator: Navigator,
) : Presenter<HomeState> {
    @Composable
    override fun present(): HomeState {
        return HomeState { event ->
            when (event) {
                is HomeUiEvent.OnCardClick -> {
                    navigator.goTo(ReaderScreen(event.cardId))
                }
            }
        }
    }
}

@Stable
data class HomeState(
    val evenSink: (HomeUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface HomeUiEvent {
    data class OnCardClick(val cardId: String) : HomeUiEvent
}
