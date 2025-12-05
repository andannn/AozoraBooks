/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.search.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.LocalNavigator
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.SearchResultScreen
import me.andannn.aozora.ui.common.retainPresenter
import org.koin.mp.KoinPlatform.getKoin

@Composable
internal fun retainSearchInputPresenter(
    initialParam: String?,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
    localNavigator: Navigator = LocalNavigator.current,
) = retainPresenter(
    initialParam,
    aozoraContentsRepository,
    localNavigator,
) {
    SearchInputPresenter(
        initialParam = initialParam,
        localNavigator = localNavigator,
    )
}

private const val TAG = "SearchInputPresenter"

private class SearchInputPresenter(
    private val initialParam: String?,
    private val localNavigator: Navigator,
) : RetainedPresenter<SearchInputState>() {
    @Composable
    override fun present(): SearchInputState {
        var inputText by remember {
            mutableStateOf(
                TextFieldValue(
                    text = initialParam ?: "",
                    selection = TextRange(initialParam?.length ?: 0),
                ),
            )
        }
        return SearchInputState(
            inputText = inputText,
        ) { event ->
            when (event) {
                SearchInputUiEvent.Back -> {
                    localNavigator.pop()
                }

                is SearchInputUiEvent.OnValueChange -> {
                    inputText = event.value
                }

                SearchInputUiEvent.OnConfirmSearch -> {
                    val query = inputText.text
                    localNavigator.pop()
                    localNavigator.goTo(SearchResultScreen(query = query))
                }
            }
        }
    }
}

@Stable
data class SearchInputState(
    val inputText: TextFieldValue,
    val evenSink: (SearchInputUiEvent) -> Unit = {},
)

sealed interface SearchInputUiEvent {
    data object Back : SearchInputUiEvent

    data object OnConfirmSearch : SearchInputUiEvent

    data class OnValueChange(
        val value: TextFieldValue,
    ) : SearchInputUiEvent
}
