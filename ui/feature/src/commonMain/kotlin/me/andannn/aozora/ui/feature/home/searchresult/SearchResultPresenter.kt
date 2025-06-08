/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.searchresult

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.async
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.common.navigator.RootNavigator
import me.andannn.aozora.ui.feature.common.screens.AuthorScreen
import me.andannn.aozora.ui.feature.common.screens.BookCardScreen
import me.andannn.aozora.ui.feature.common.screens.SearchInputScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberSearchResultPresenter(
    query: String,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
    localNavigator: Navigator = LocalNavigator.current,
    rootNavigator: Navigator = RootNavigator.current,
) = remember(
    query,
    aozoraContentsRepository,
    localNavigator,
    rootNavigator,
) {
    SearchResultPresenter(
        query = query,
        aozoraContentsRepository = aozoraContentsRepository,
        localNavigator = localNavigator,
        rootNavigator = rootNavigator,
    )
}

private const val TAG = "SearchResultPresenter"

class SearchResultPresenter(
    private val query: String,
    private val aozoraContentsRepository: AozoraContentsRepository,
    private val localNavigator: Navigator,
    private val rootNavigator: Navigator,
) : Presenter<SearchResultState> {
    @Composable
    override fun present(): SearchResultState {
        val loadState by produceRetainedState<LoadState>(
            initialValue = LoadState.Loading,
        ) {
            val bookResultDeferred =
                async {
                    aozoraContentsRepository.searchBooks("$query*")
                }
            val authorResultDeferred =
                async {
                    aozoraContentsRepository.searchAuthors("$query*")
                }
            val bookResult = bookResultDeferred.await()
            val authorResult = authorResultDeferred.await()
            value =
                if (bookResult.isEmpty() && authorResult.isEmpty()) {
                    LoadState.NoResult
                } else {
                    LoadState.Result(
                        books = bookResult,
                        authors = authorResult,
                    )
                }
        }

        return SearchResultState(
            query = query,
            loadState = loadState,
        ) { event ->
            when (event) {
                SearchResultUiEvent.Back -> localNavigator.pop()
                is SearchResultUiEvent.OnAuthorClick -> localNavigator.goTo(AuthorScreen(event.author.authorId))
                is SearchResultUiEvent.OnBookClick ->
                    localNavigator.goTo(
                        BookCardScreen(
                            event.book.id,
                            event.book.authorId,
                        ),
                    )

                SearchResultUiEvent.OnTitleClick -> {
                    localNavigator.goTo(
                        SearchInputScreen(initialParam = query),
                    )
                }
            }
        }
    }
}

@Stable
data class SearchResultState(
    val query: String,
    val loadState: LoadState,
    val evenSink: (SearchResultUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface LoadState {
    data object Loading : LoadState

    data class Result(
        val books: List<AozoraBookCard>,
        val authors: List<AuthorData>,
    ) : LoadState

    data object NoResult : LoadState
}

sealed interface SearchResultUiEvent {
    data object Back : SearchResultUiEvent

    data object OnTitleClick : SearchResultUiEvent

    data class OnAuthorClick(
        val author: AuthorData,
    ) : SearchResultUiEvent

    data class OnBookClick(
        val book: AozoraBookCard,
    ) : SearchResultUiEvent
}
