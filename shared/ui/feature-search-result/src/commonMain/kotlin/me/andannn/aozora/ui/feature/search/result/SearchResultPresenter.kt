/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.search.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.andannn.RetainedModel
import io.github.andannn.retainRetainedModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.AuthorScreen
import me.andannn.aozora.ui.common.BookCardScreen
import me.andannn.aozora.ui.common.LocalNavigator
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.Presenter
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.RootNavigator
import me.andannn.aozora.ui.common.SearchInputScreen
import me.andannn.aozora.ui.common.retainPresenter
import org.koin.mp.KoinPlatform.getKoin

@Composable
internal fun retainSearchResultPresenter(
    query: String,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
    localNavigator: Navigator = LocalNavigator.current,
    rootNavigator: Navigator = RootNavigator.current,
) = retainPresenter(
    query,
    aozoraContentsRepository,
    localNavigator,
    rootNavigator,
) {
    SearchResultPresenter(
        query = query,
        aozoraContentsRepository = aozoraContentsRepository,
        localNavigator = localNavigator,
    )
}

private const val TAG = "SearchResultPresenter"

private class SearchResultPresenter(
    private val query: String,
    private val aozoraContentsRepository: AozoraContentsRepository,
    private val localNavigator: Navigator,
) : RetainedPresenter<SearchResultState>() {
    val loadStateFlow = MutableStateFlow<LoadState>(LoadState.Loading)

    init {
        retainedScope.launch {
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
            loadStateFlow.value =
                if (bookResult.isEmpty() && authorResult.isEmpty()) {
                    LoadState.NoResult
                } else {
                    LoadState.Result(
                        books = bookResult,
                        authors = authorResult,
                    )
                }
        }
    }

    @Composable
    override fun present(): SearchResultState {
        val searchCategories =
            remember {
                mutableStateListOf(SearchCategory.BOOK, SearchCategory.AUTHOR)
            }
        val loadState by loadStateFlow.collectAsStateWithLifecycle()

        return SearchResultState(
            searchCategory = searchCategories.toImmutableList(),
            query = query,
            loadState = loadState,
        ) { event ->
            when (event) {
                SearchResultUiEvent.Back -> {
                    localNavigator.pop()
                }

                is SearchResultUiEvent.OnAuthorClick -> {
                    localNavigator.goTo(AuthorScreen(event.author.authorId))
                }

                is SearchResultUiEvent.OnBookClick -> {
                    localNavigator.goTo(
                        BookCardScreen(
                            event.book.id,
                            event.book.authorId,
                        ),
                    )
                }

                SearchResultUiEvent.OnTitleClick -> {
                    localNavigator.goTo(
                        SearchInputScreen(initialParam = query),
                    )
                }

                is SearchResultUiEvent.OnSearchCategoryChange -> {
                    if (event.searchCategory !in searchCategories) {
                        searchCategories.add(event.searchCategory)
                    } else {
                        searchCategories.remove(event.searchCategory)
                    }
                }
            }
        }
    }
}

enum class SearchCategory {
    BOOK,
    AUTHOR,
}

@Stable
data class SearchResultState(
    val query: String,
    val searchCategory: ImmutableList<SearchCategory>,
    val loadState: LoadState,
    val evenSink: (SearchResultUiEvent) -> Unit = {},
)

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

    data class OnSearchCategoryChange(
        val searchCategory: SearchCategory,
    ) : SearchResultUiEvent

    data class OnAuthorClick(
        val author: AuthorData,
    ) : SearchResultUiEvent

    data class OnBookClick(
        val book: AozoraBookCard,
    ) : SearchResultUiEvent
}
