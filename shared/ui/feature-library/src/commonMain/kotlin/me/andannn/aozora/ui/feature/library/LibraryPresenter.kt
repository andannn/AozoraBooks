/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.BookWithProgress
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.AboutScreen
import me.andannn.aozora.ui.common.BookCardScreen
import me.andannn.aozora.ui.common.LocalNavigator
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.ReaderScreen
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.RootNavigator
import me.andannn.aozora.ui.common.SearchNestedScreen
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.OnClickOption
import me.andannn.aozora.ui.common.dialog.OptionItem
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.dialog.showBookOptionDialog
import me.andannn.aozora.ui.common.retainPresenter
import org.koin.mp.KoinPlatform.getKoin

private const val TAG = "LibraryPresenter"

@Composable
fun retainLibraryPresenter(
    localNavigator: Navigator = LocalNavigator.current,
    userDataRepository: UserDataRepository = getKoin().get(),
    rootNavigator: Navigator = RootNavigator.current,
    uriHandler: UriHandler = LocalUriHandler.current,
    popupController: PopupController = LocalPopupController.current,
) = retainPresenter(
    localNavigator,
    userDataRepository,
    rootNavigator,
    uriHandler,
    popupController,
) {
    Napier.d(tag = TAG) { "localNavigator: ${localNavigator.hashCode()}" }
    Napier.d(tag = TAG) { "userDataRepository: ${userDataRepository.hashCode()}" }
    Napier.d(tag = TAG) { "rootNavigator: ${rootNavigator.hashCode()}" }
    Napier.d(tag = TAG) { "uriHandler: ${uriHandler.hashCode()}" }
    Napier.d(tag = TAG) { "popupController: ${popupController.hashCode()}" }

    LibraryPresenter(localNavigator, userDataRepository, rootNavigator, uriHandler, popupController)
}

private class LibraryPresenter(
    private val localNavigator: Navigator,
    private val userDataRepository: UserDataRepository,
    private val rootNavigator: Navigator,
    private val uriHandler: UriHandler,
    private val popupController: PopupController,
) : RetainedPresenter<LibraryState>() {
    init {
        Napier.d(tag = TAG) { "init" }
    }

    val notCompletedBooksFlow =
        userDataRepository
            .getAllNotCompletedBooks()
            .stateIn(
                retainedScope,
                initialValue = emptyList(),
                started =
                    SharingStarted
                        .WhileSubscribed(5000),
            )
    val completedBooksFlow =
        userDataRepository
            .getAllCompletedBooks()
            .stateIn(
                retainedScope,
                initialValue = emptyList(),
                started =
                    SharingStarted
                        .WhileSubscribed(5000),
            )

    val currentTabFlow = MutableStateFlow(TabItem.READING)

    @Composable
    override fun present(): LibraryState {
        val notCompletedBooks by notCompletedBooksFlow.collectAsStateWithLifecycle()
        val completedBooks by completedBooksFlow.collectAsStateWithLifecycle()
        val currentTab by currentTabFlow.collectAsStateWithLifecycle()

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
                    currentTabFlow.value = event.item
                }

                LibraryUiEvent.OnGoToSearch -> {
                    localNavigator.goTo(SearchNestedScreen)
                }

                is LibraryUiEvent.OnCardOptionClick -> {
                    retainedScope.launch {
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
)

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
