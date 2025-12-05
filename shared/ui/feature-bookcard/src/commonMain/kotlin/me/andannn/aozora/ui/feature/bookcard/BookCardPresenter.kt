/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.bookcard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.AuthorScreen
import me.andannn.aozora.ui.common.LocalNavigator
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.NdcContentScreen
import me.andannn.aozora.ui.common.ReaderScreen
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.RootNavigator
import me.andannn.aozora.ui.common.retainPresenter
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun retainBookCardPresenter(
    groupId: String,
    bookId: String,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
    userDataRepository: UserDataRepository = getKoin().get(),
    rootNavigator: Navigator = RootNavigator.current,
    localNavigator: Navigator = LocalNavigator.current,
) = retainPresenter(
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

@Stable
data class BookCardState(
    val bookCardInfo: AozoraBookCard?,
    val ndcClassificationToDescription: Map<NDCClassification, String>,
    val isAddedToShelf: Boolean,
    val evenSink: (BookCardUiEvent) -> Unit = {},
)

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

private const val TAG = "BookCardPresenter"

private class BookCardPresenter(
    private val groupId: String,
    private val bookId: String,
    private val aozoraContentsRepository: AozoraContentsRepository,
    private val rootNavigator: Navigator,
    private val localNavigator: Navigator,
    private val userDataRepository: UserDataRepository,
) : RetainedPresenter<BookCardState>() {
    val bookCardInfoFlow = MutableStateFlow<AozoraBookCard?>(null)
    val savedBookCardFlow =
        userDataRepository
            .getSavedBookById(bookId, authorId = groupId)
            .stateIn(
                retainedScope,
                initialValue = null,
                started = SharingStarted.WhileSubscribed(5000),
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val ndcClassificationToDescriptionFlow =
        bookCardInfoFlow
            .mapLatest { bookCardInfoOrNull ->
                val categories = bookCardInfoOrNull?.categories ?: emptyList()
                categories
                    .distinct()
                    .associateWith { ndcClassification ->
                        aozoraContentsRepository.getNDCDetails(ndcClassification)?.label ?: ""
                    }
            }.stateIn(
                retainedScope,
                initialValue = emptyMap(),
                started = SharingStarted.WhileSubscribed(5000),
            )

    init {
        retainedScope.launch {
            aozoraContentsRepository
                .getBookCard(
                    cardId = bookId,
                    authorId = groupId,
                ).filterNotNull()
                .collect {
                    bookCardInfoFlow.value = it
                }
        }
    }

    @Composable
    override fun present(): BookCardState {
        val bookCardInfo by bookCardInfoFlow.collectAsStateWithLifecycle()
        val savedBookCard by savedBookCardFlow.collectAsStateWithLifecycle()
        val ndcClassificationToDescription by ndcClassificationToDescriptionFlow.collectAsStateWithLifecycle()

        return BookCardState(
            bookCardInfo = bookCardInfo,
            ndcClassificationToDescription = ndcClassificationToDescription,
            isAddedToShelf = savedBookCard != null,
        ) { event ->
            when (event) {
                BookCardUiEvent.Back -> {
                    localNavigator.pop()
                }

                BookCardUiEvent.OnAddToShelf -> {
                    retainedScope.launch {
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
