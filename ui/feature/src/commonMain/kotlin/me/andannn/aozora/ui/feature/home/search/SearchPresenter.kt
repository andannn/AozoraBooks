/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.screens.IndexPageScreen

@Composable
fun rememberSearchPresenter(
    nestedNavigator: Navigator,
    navigator: Navigator = LocalNavigator.current,
): SearchPresenter =
    remember(
        navigator,
    ) {
        SearchPresenter(
            navigator = navigator,
        )
    }

class SearchPresenter(
    private val navigator: Navigator,
) : Presenter<SearchState> {
    @Composable
    override fun present(): SearchState =
        SearchState { event ->
            when (event) {
                is SearchUiEvent.OnClickKanaItem -> {
                    navigator.goTo(IndexPageScreen(kana = event.kana))
                }
            }
        }
}

@Stable
data class SearchState(
    val evenSink: (SearchUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface SearchUiEvent {
    data class OnClickKanaItem(
        val kana: String,
    ) : SearchUiEvent
}
