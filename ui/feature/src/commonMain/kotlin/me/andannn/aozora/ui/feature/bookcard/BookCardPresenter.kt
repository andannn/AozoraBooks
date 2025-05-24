/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.bookcard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.LoadResult
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.screens.ReaderScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberBookCardPresenter(
    groupId: String,
    bookId: String,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
    userDataRepository: UserDataRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = remember(
    groupId,
    bookId,
    aozoraContentsRepository,
    userDataRepository,
    navigator,
) {
    BookCardPresenter(
        groupId = groupId,
        bookId = bookId,
        aozoraContentsRepository = aozoraContentsRepository,
        userDataRepository = userDataRepository,
        navigator = navigator,
    )
}

private const val TAG = "BookCardPresenter"

class BookCardPresenter(
    private val groupId: String,
    private val bookId: String,
    private val aozoraContentsRepository: AozoraContentsRepository,
    private val navigator: Navigator,
    private val userDataRepository: UserDataRepository,
) : Presenter<BookCardState> {
    @Composable
    override fun present(): BookCardState {
        val scope = rememberCoroutineScope()
        var bookCardInfo by rememberRetained {
            mutableStateOf<AozoraBookCard?>(null)
        }
        val savedBookCard by userDataRepository
            .getSavedBookById(bookId)
            .collectAsRetainedState(null)
        LaunchedEffect(Unit) {
            Napier.d(tag = TAG) { "present groupId $groupId, bookId $bookId" }
            val result = aozoraContentsRepository.getBookCard(cardId = bookId, groupId = groupId)
            when (result) {
                is LoadResult.Error -> {
                    Napier.e(tag = TAG) { "present error ${result.throwable}" }
                }

                is LoadResult.Success -> {
                    bookCardInfo = result.data
                }
            }
        }
        return BookCardState(
            bookCardInfo = bookCardInfo,
            isAddedToShelf = savedBookCard != null,
        ) { event ->
            when (event) {
                BookCardUiEvent.Back -> navigator.pop()
                BookCardUiEvent.OnAddToShelf -> {
                    scope.launch {
                        if (savedBookCard != null) {
                            userDataRepository.deleteSavedBook(
                                savedBookCard!!.id,
                            )
                        } else {
                            userDataRepository.saveBookToLibrary(
                                bookCardInfo?.id ?: error("bookCardInfo is null"),
                            )
                        }
                    }
                }

                BookCardUiEvent.OnClickRead -> {
                    navigator.goTo(
                        ReaderScreen(cardId = bookId),
                    )
                }
            }
        }
    }
}

@Stable
data class BookCardState(
    val bookCardInfo: AozoraBookCard?,
    val isAddedToShelf: Boolean,
    val evenSink: (BookCardUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface BookCardUiEvent {
    data object Back : BookCardUiEvent

    data object OnAddToShelf : BookCardUiEvent

    data object OnClickRead : BookCardUiEvent
}
