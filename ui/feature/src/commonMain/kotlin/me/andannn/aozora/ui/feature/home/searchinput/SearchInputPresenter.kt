/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.searchinput

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.common.screens.SearchResultScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberSearchInputPresenter(
    initialParam: String?,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
    localNavigator: Navigator = LocalNavigator.current,
) = remember(
    initialParam,
    aozoraContentsRepository,
    localNavigator,
) {
    SearchInputPresenter(
        initialParam = initialParam,
        aozoraContentsRepository = aozoraContentsRepository,
        localNavigator = localNavigator,
    )
}

private const val TAG = "SearchInputPresenter"

class SearchInputPresenter(
    private val initialParam: String?,
    private val aozoraContentsRepository: AozoraContentsRepository,
    private val localNavigator: Navigator,
) : Presenter<SearchInputState> {
    @Composable
    override fun present(): SearchInputState {
        var inputText by rememberSaveable { mutableStateOf(initialParam) }
        return SearchInputState(
            inputText = inputText,
        ) { event ->
            when (event) {
                SearchInputUiEvent.Back -> localNavigator.pop()
                is SearchInputUiEvent.OnValueChange -> {
                    inputText = event.value
                }

                SearchInputUiEvent.OnConfirmSearch -> {
                    val query = inputText ?: return@SearchInputState
                    localNavigator.pop()
                    localNavigator.goTo(SearchResultScreen(query = query))
                }
            }
        }
    }
}

@Stable
data class SearchInputState(
    val inputText: String?,
    val evenSink: (SearchInputUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface SearchInputUiEvent {
    data object Back : SearchInputUiEvent

    data object OnConfirmSearch : SearchInputUiEvent

    data class OnValueChange(
        val value: String,
    ) : SearchInputUiEvent
}
