package me.andannn.aozora.ui.feature.home.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.home.NavigationItem
import me.andannn.aozora.ui.feature.screens.ReaderScreen

@Composable
fun rememberLibraryPresenter(navigator: Navigator = LocalNavigator.current) =
    remember(
        navigator,
    ) {
        LibraryPresenter(navigator)
    }

class LibraryPresenter(
    private val navigator: Navigator,
) : Presenter<LibraryState> {
    @Composable
    override fun present(): LibraryState {
        var navigationItem by remember {
            mutableStateOf(NavigationItem.LIBRARY)
        }

        return LibraryState(
            currentNavigation = navigationItem,
        ) { event ->
            when (event) {
                is LibraryUiEvent.OnCardClick -> {
                    navigator.goTo(ReaderScreen(event.cardId))
                }
            }
        }
    }
}

@Stable
data class LibraryState(
    val currentNavigation: NavigationItem,
    val evenSink: (LibraryUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface LibraryUiEvent {
    data class OnCardClick(
        val cardId: String,
    ) : LibraryUiEvent
}
