/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.common.navigator.RootNavigator
import me.andannn.aozora.ui.feature.common.screens.AuthorPagesScreen
import me.andannn.aozora.ui.feature.common.screens.IndexPageScreen
import me.andannn.aozora.ui.feature.common.screens.SearchInputResult
import me.andannn.aozora.ui.feature.common.screens.SearchInputScreen

@Composable
fun rememberSearchPresenter(
    rootNavigator: Navigator = RootNavigator.current,
    localNavigator: Navigator = LocalNavigator.current,
): SearchPresenter =
    remember(
        rootNavigator,
        localNavigator,
    ) {
        SearchPresenter(
            rootNavigator = rootNavigator,
            localNavigator = localNavigator,
        )
    }

private const val TAG = "SearchPresenter"

class SearchPresenter(
    private val rootNavigator: Navigator,
    private val localNavigator: Navigator,
) : Presenter<SearchState> {
    @Composable
    override fun present(): SearchState {
        val searchInputNavigator =
            rememberAnsweringNavigator<SearchInputResult>(localNavigator) { result ->
                Napier.d(tag = TAG) { "navigator result: ${result.inputText}" }
                // Navigate to search result Page.
            }

        return SearchState { event ->
            when (event) {
                is SearchUiEvent.OnClickKanaItem -> {
                    rootNavigator.goTo(IndexPageScreen(kana = event.kana.kanaLabel))
                }

                is SearchUiEvent.OnClickKanaLineItem -> {
                    rootNavigator.goTo(AuthorPagesScreen(code = event.lineItem.code))
                }

                SearchUiEvent.OnSearchBarClick -> {
                    searchInputNavigator.goTo(SearchInputScreen(initialParam = null))
                }
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
        val kana: KanaItem,
    ) : SearchUiEvent

    data class OnClickKanaLineItem(
        val lineItem: KanaLineItem,
    ) : SearchUiEvent

    data object OnSearchBarClick : SearchUiEvent
}
