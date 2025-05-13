/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.screens.ReaderScreen

@Composable
fun rememberHomePresenter(
    navigator: Navigator = LocalNavigator.current,
    popupController: PopupController = LocalPopupController.current,
) = remember(
    navigator,
    popupController,
) {
    HomePresenter(
        navigator = navigator,
        popupController = popupController,
    )
}

class HomePresenter(
    private val navigator: Navigator,
    private val popupController: PopupController,
) : Presenter<HomeState> {
    @Composable
    override fun present(): HomeState {
        var navigationItem by rememberRetained {
            mutableStateOf(NavigationItem.LIBRARY)
        }

        return HomeState(
            currentNavigation = navigationItem,
        ) { event ->
            when (event) {
                is HomeUiEvent.OnCardClick -> {
                    navigator.goTo(ReaderScreen(event.cardId))
                }

                is HomeUiEvent.OnNavigationItemClick -> {
                    navigationItem = event.item
                }

                HomeUiEvent.OnClickMore -> {
//                    popupController.showDialog(DialogId)
                }
            }
        }
    }
}

@Stable
data class HomeState(
    val currentNavigation: NavigationItem,
    val evenSink: (HomeUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface HomeUiEvent {
    data class OnCardClick(
        val cardId: String,
    ) : HomeUiEvent

    data class OnNavigationItemClick(
        val item: NavigationItem,
    ) : HomeUiEvent

    data object OnClickMore : HomeUiEvent
}
