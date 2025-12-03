/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.bookcard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.common.navigator.RootNavigator
import me.andannn.aozora.ui.feature.common.screens.AuthorScreen
import me.andannn.aozora.ui.feature.common.screens.NdcContentScreen
import me.andannn.aozora.ui.feature.common.screens.ReaderScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberBookCardPresenter(
    groupId: String,
    bookId: String,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
    userDataRepository: UserDataRepository = getKoin().get(),
    rootNavigator: Navigator = RootNavigator.current,
    localNavigator: Navigator = LocalNavigator.current,
) = remember(
    groupId,
    bookId,
    aozoraContentsRepository,
    userDataRepository,
    rootNavigator,
    localNavigator,
) {
    BookCardPresenter(
        groupId = groupId,
        bookId = bookId,
        aozoraContentsRepository = aozoraContentsRepository,
        userDataRepository = userDataRepository,
        rootNavigator = rootNavigator,
        localNavigator = localNavigator,
    )
}

private const val TAG = "BookCardPresenter"

class BookCardPresenter(
    private val groupId: String,
    private val bookId: String,
    private val aozoraContentsRepository: AozoraContentsRepository,
    private val rootNavigator: Navigator,
    private val localNavigator: Navigator,
    private val userDataRepository: UserDataRepository,
) : Presenter<BookCardState> {
    @Composable
    override fun present(): BookCardState {
        val scope = rememberCoroutineScope()
        val bookCardInfo by
            produceRetainedState<AozoraBookCard?>(null) {
                aozoraContentsRepository
                    .getBookCard(
                        cardId = bookId,
                        authorId = groupId,
                    ).filterNotNull()
                    .collect {
                        value = it
                    }
            }
        val savedBookCard by userDataRepository
            .getSavedBookById(bookId, authorId = groupId)
            .collectAsRetainedState(null)

        val ndcClassificationToDescription by produceRetainedState(emptyMap(), bookCardInfo) {
            val categories = bookCardInfo?.categories ?: emptyList()
            value =
                categories
                    .distinct()
                    .associateWith { ndcClassification ->
                        aozoraContentsRepository.getNDCDetails(ndcClassification)?.label ?: ""
                    }
        }

        return BookCardState(
            bookCardInfo = bookCardInfo,
            ndcClassificationToDescription = ndcClassificationToDescription,
            isAddedToShelf = savedBookCard != null,
        ) { event ->
            when (event) {
                BookCardUiEvent.Back -> localNavigator.pop()
                BookCardUiEvent.OnAddToShelf -> {
                    scope.launch {
                        if (savedBookCard != null) {
                            userDataRepository.deleteSavedBook(
                                savedBookCard!!.id,
                                savedBookCard!!.authorId,
                            )
                        } else {
                            userDataRepository.saveBookToLibrary(
                                bookCardInfo?.id ?: error("bookCardInfo is null"),
                                bookCardInfo?.authorId ?: error("bookCardInfo is null"),
                            )
                        }
                    }
                }

                BookCardUiEvent.OnClickRead -> {
                    rootNavigator.goTo(
                        ReaderScreen(cardId = bookId, authorId = groupId),
                    )
                }

                is BookCardUiEvent.OnClickAuthor -> {
                    localNavigator.goTo(
                        AuthorScreen(authorId = event.authorId),
                    )
                }

                is BookCardUiEvent.OnNdcClick -> {
                    localNavigator.goTo(
                        NdcContentScreen(
                            ndc = event.ndcClassification.value,
                        ),
                    )
                }
            }
        }
    }
}

@Stable
data class BookCardState(
    val bookCardInfo: AozoraBookCard?,
    val ndcClassificationToDescription: Map<NDCClassification, String>,
    val isAddedToShelf: Boolean,
    val evenSink: (BookCardUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface BookCardUiEvent {
    data object Back : BookCardUiEvent

    data object OnAddToShelf : BookCardUiEvent

    data object OnClickRead : BookCardUiEvent

    data class OnNdcClick(
        val ndcClassification: NDCClassification,
    ) : BookCardUiEvent

    data class OnClickAuthor(
        val authorId: String,
    ) : BookCardUiEvent
}
