/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.BookModelTemp
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.screens.ReaderScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberLibraryPresenter(
    userDataRepository: UserDataRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = remember(
    userDataRepository,
    navigator,
) {
    LibraryPresenter(userDataRepository, navigator)
}

class LibraryPresenter(
    private val userDataRepository: UserDataRepository,
    private val navigator: Navigator,
) : Presenter<LibraryState> {
    @Composable
    override fun present(): LibraryState {
        val savedBooks by userDataRepository
            .getAllSavedBook()
            .collectAsRetainedState(initial = emptyList())
        return LibraryState(
            savedBooks = savedBooks.toImmutableList(),
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
    val savedBooks: ImmutableList<BookModelTemp>,
    val evenSink: (LibraryUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface LibraryUiEvent {
    data class OnCardClick(
        val cardId: String,
    ) : LibraryUiEvent
}
