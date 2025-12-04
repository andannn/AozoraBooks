/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import io.github.andannn.RetainedModel
import io.github.andannn.retainRetainedModel
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.Presenter
import me.andannn.aozora.ui.common.RootNavigator

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
)

sealed interface HomeUiEvent
