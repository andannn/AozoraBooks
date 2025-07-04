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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.BookWithProgress
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.common.navigator.RootNavigator
import me.andannn.aozora.ui.feature.common.dialog.OnClickOption
import me.andannn.aozora.ui.feature.common.dialog.OptionItem
import me.andannn.aozora.ui.feature.common.dialog.showBookOptionDialog
import me.andannn.aozora.ui.feature.common.screens.AboutScreen
import me.andannn.aozora.ui.feature.common.screens.BookCardScreen
import me.andannn.aozora.ui.feature.common.screens.ReaderScreen
import me.andannn.aozora.ui.feature.common.screens.SearchNestedScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberLibraryPresenter(
    localNavigator: Navigator = LocalNavigator.current,
    userDataRepository: UserDataRepository = getKoin().get(),
    rootNavigator: Navigator = RootNavigator.current,
    uriHandler: UriHandler = LocalUriHandler.current,
    popupController: PopupController = LocalPopupController.current,
) = remember(
    localNavigator,
    userDataRepository,
    rootNavigator,
    uriHandler,
    popupController,
) {
    LibraryPresenter(localNavigator, userDataRepository, rootNavigator, uriHandler, popupController)
}

class LibraryPresenter(
    private val localNavigator: Navigator,
    private val userDataRepository: UserDataRepository,
    private val rootNavigator: Navigator,
    private val uriHandler: UriHandler,
    private val popupController: PopupController,
) : Presenter<LibraryState> {
    @Composable
    override fun present(): LibraryState {
        val notCompletedBooks by userDataRepository
            .getAllNotCompletedBooks()
            .collectAsRetainedState(initial = emptyList())
        val completedBooks by userDataRepository
            .getAllCompletedBooks()
            .collectAsRetainedState(initial = emptyList())
        var currentTab by rememberRetained {
            mutableStateOf(TabItem.READING)
        }
        val scope = rememberCoroutineScope()
        return LibraryState(
            currentTab = currentTab,
            notCompletedBooks = notCompletedBooks.toImmutableList(),
            completedBooks = completedBooks.toImmutableList(),
        ) { event ->
            when (event) {
                is LibraryUiEvent.OnCardClick -> {
                    rootNavigator.goTo(ReaderScreen(event.card.id, event.card.authorId))
                }

                is LibraryUiEvent.OnTabRowClick -> {
                    currentTab = event.item
                }

                LibraryUiEvent.OnGoToSearch -> {
                    localNavigator.goTo(SearchNestedScreen)
                }

                is LibraryUiEvent.OnCardOptionClick -> {
                    scope.launch {
                        val result =
                            popupController.showBookOptionDialog(
                                fromReadingTab = currentTab == TabItem.READING,
                            )
                        if (result is OnClickOption) {
                            when (result.option) {
                                OptionItem.OPEN_BOOK_CARD -> {
                                    localNavigator.goTo(
                                        BookCardScreen(
                                            bookCardId = event.card.id,
                                            groupId = event.card.authorId,
                                        ),
                                    )
                                }

                                OptionItem.REMOVE_FROM_BOOK_SHELF -> {
                                    userDataRepository.deleteSavedBook(
                                        event.card.id,
                                        event.card.authorId,
                                    )
                                }

                                OptionItem.MARK_AS_COMPLETED -> {
                                    userDataRepository.markBookAsCompleted(
                                        event.card.id,
                                        event.card.authorId,
                                    )
                                }

                                OptionItem.MARK_AS_NOT_COMPLETED -> {
                                    userDataRepository.markBookAsNotCompleted(event.card.id)
                                }

                                OptionItem.OPEN_AOZORA_BOOK_CARD_WEB_PAGE -> {
                                    uriHandler.openUri(event.card.cardUrl)
                                }
                            }
                        }
                    }
                }

                LibraryUiEvent.OnClickMore -> {
                    rootNavigator.goTo(AboutScreen)
                }
            }
        }
    }
}

@Stable
data class LibraryState(
    val currentTab: TabItem,
    val notCompletedBooks: ImmutableList<BookWithProgress>,
    val completedBooks: ImmutableList<BookWithProgress>,
    val evenSink: (LibraryUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface LibraryUiEvent {
    data class OnCardClick(
        val card: AozoraBookCard,
    ) : LibraryUiEvent

    data class OnCardOptionClick(
        val card: AozoraBookCard,
    ) : LibraryUiEvent

    data class OnTabRowClick(
        val item: TabItem,
    ) : LibraryUiEvent

    data object OnGoToSearch : LibraryUiEvent

    data object OnClickMore : LibraryUiEvent
}

enum class TabItem {
    READING,
    READ_COMPLETE,
}
