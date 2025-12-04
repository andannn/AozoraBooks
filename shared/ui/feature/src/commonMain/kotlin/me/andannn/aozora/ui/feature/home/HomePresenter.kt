/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.andannn.RetainedModel
import io.github.andannn.retainRetainedModel
import me.andannn.aozora.ui.common.navigator.RootNavigator

@Composable
fun retainHomePresenter(navigator: Navigator = RootNavigator.current) =
    retainRetainedModel(
        navigator,
    ) {
        HomePresenter(
            navigator = navigator,
        )
    }

class HomePresenter(
    private val navigator: Navigator,
) : RetainedModel(),
    Presenter<HomeState> {
    @Composable
    override fun present(): HomeState =
        HomeState { event ->
        }
}

@Stable
data class HomeState(
    val evenSink: (HomeUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface HomeUiEvent
