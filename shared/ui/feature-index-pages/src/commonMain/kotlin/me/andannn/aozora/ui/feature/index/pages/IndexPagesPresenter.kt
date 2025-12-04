/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.index.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.aakira.napier.Napier
import io.github.andannn.RetainedModel
import io.github.andannn.retainRetainedModel
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.BookCardScreen
import me.andannn.aozora.ui.common.LocalNavigator
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.Presenter
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.retainPresenter
import org.koin.mp.KoinPlatform.getKoin

@Stable
data class IndexPagesState(
    val kanaLabel: String,
    val pagingData: LazyPagingItems<AozoraBookCard>,
    val evenSink: (IndexPagesUiEvent) -> Unit = {},
)

sealed interface IndexPagesUiEvent {
    data object OnBack : IndexPagesUiEvent

    data class OnBookClick(
        val book: AozoraBookCard,
    ) : IndexPagesUiEvent
}

@Composable
internal fun retainIndexPagesPresenter(
    kana: String,
    aozoraRepository: AozoraContentsRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = retainPresenter(
    kana,
    aozoraRepository,
    navigator,
) {
    IndexPagesPresenter(kana, aozoraRepository, navigator)
}

private const val TAG = "IndexPagesPresenter"

private class IndexPagesPresenter(
    private val kana: String,
    private val aozoraRepository: AozoraContentsRepository,
    private val navigator: Navigator,
) : RetainedPresenter<IndexPagesState>() {
    val pagingDataFlow =
        aozoraRepository.getBookListPagingFlow(kana).cachedIn(retainedScope)

    @Composable
    override fun present(): IndexPagesState {
        Napier.d(tag = TAG) { "present $kana" }
        val pagingData = pagingDataFlow.collectAsLazyPagingItems()
        return IndexPagesState(
            kana,
            pagingData,
        ) { event ->
            when (event) {
                IndexPagesUiEvent.OnBack -> {
                    navigator.pop()
                }

                is IndexPagesUiEvent.OnBookClick -> {
                    Napier.d(tag = TAG) { "goto book card ${event.book.id} groupId ${event.book.authorId}" }
                    navigator.goTo(
                        BookCardScreen(
                            bookCardId = event.book.id,
                            groupId = event.book.authorId,
                        ),
                    )
                }
            }
        }
    }
}
