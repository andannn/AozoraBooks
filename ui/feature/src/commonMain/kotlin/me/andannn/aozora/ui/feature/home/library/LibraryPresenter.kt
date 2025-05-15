/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.BookModelTemp
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.dialog.BookOptionDialogId
import me.andannn.aozora.ui.feature.dialog.OnClickOption
import me.andannn.aozora.ui.feature.dialog.OptionItem
import me.andannn.aozora.ui.feature.home.SearchNestedScreen
import me.andannn.aozora.ui.feature.screens.BookCardScreen
import me.andannn.aozora.ui.feature.screens.ReaderScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberLibraryPresenter(
    nestedNavigator: Navigator,
    userDataRepository: UserDataRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
    popupController: PopupController = LocalPopupController.current,
) = remember(
    nestedNavigator,
    userDataRepository,
    navigator,
    popupController,
) {
    LibraryPresenter(nestedNavigator, userDataRepository, navigator, popupController)
}

class LibraryPresenter(
    private val nestedNavigator: Navigator,
    private val userDataRepository: UserDataRepository,
    private val navigator: Navigator,
    private val popupController: PopupController,
) : Presenter<LibraryState> {
    @Composable
    override fun present(): LibraryState {
        val savedBooks by userDataRepository
            .getAllSavedBook()
            .collectAsRetainedState(initial = emptyList())
        var currentTab by rememberRetained {
            mutableStateOf(TabItem.BOOK_SHELF)
        }
        val scope = rememberCoroutineScope()
        return LibraryState(
            currentTab = currentTab,
            savedBooks = savedBooks.toImmutableList(),
        ) { event ->
            when (event) {
                is LibraryUiEvent.OnCardClick -> {
                    navigator.goTo(ReaderScreen(event.cardId))
                }

                is LibraryUiEvent.OnTabRowClick -> {
                    currentTab = event.item
                }

                LibraryUiEvent.OnGoToSearch -> {
                    nestedNavigator.goTo(SearchNestedScreen)
                }

                is LibraryUiEvent.OnCardOptionClick -> {
                    scope.launch {
                        val result = popupController.showDialog(BookOptionDialogId)
                        if (result is OnClickOption) {
                            when (result.option) {
                                OptionItem.OPEN_BOOK_CARD -> {
                                    navigator.goTo(
                                        BookCardScreen(
                                            bookCardId = event.card.id,
                                            groupId = event.card.groupId,
                                        ),
                                    )
                                }

                                OptionItem.REMOVE_FROM_BOOK_SHELF -> {
                                    userDataRepository.deleteSavedBook(event.card.id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Stable
data class LibraryState(
    val currentTab: TabItem,
    val savedBooks: ImmutableList<BookModelTemp>,
    val evenSink: (LibraryUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface LibraryUiEvent {
    data class OnCardClick(
        val cardId: String,
    ) : LibraryUiEvent

    data class OnCardOptionClick(
        val card: BookModelTemp,
    ) : LibraryUiEvent

    data class OnTabRowClick(
        val item: TabItem,
    ) : LibraryUiEvent

    data object OnGoToSearch : LibraryUiEvent
}

enum class TabItem {
    BOOK_SHELF,
    READ_COMPLETE,
}
