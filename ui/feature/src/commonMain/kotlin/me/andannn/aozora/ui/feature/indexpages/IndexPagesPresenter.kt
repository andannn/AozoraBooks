/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.indexpages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.domain.model.BookColumnItem
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.screens.BookCardScreen
import me.andannn.core.util.rememberRetainedCoroutineScope
import me.andannn.core.util.romajiToKana
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberIndexPagesPresenter(
    kana: String,
    aozoraRepository: AozoraContentsRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = remember(
    kana,
    aozoraRepository,
    navigator,
) {
    IndexPagesPresenter(kana, aozoraRepository, navigator)
}

private const val TAG = "IndexPagesPresenter"

class IndexPagesPresenter(
    private val kana: String,
    private val aozoraRepository: AozoraContentsRepository,
    private val navigator: Navigator,
) : Presenter<IndexPagesState> {
    @Composable
    override fun present(): IndexPagesState {
        val kanaLabel =
            remember {
                kana.romajiToKana(isKatakana = true)
            }
        val scope = rememberRetainedCoroutineScope()
        val pagingDataFlow =
            rememberRetained {
                aozoraRepository.getBookListPagingFlow(kana).cachedIn(scope)
            }
        val pagingData = pagingDataFlow.collectAsLazyPagingItems()
        return IndexPagesState(
            kanaLabel,
            pagingData,
        ) { event ->
            when (event) {
                IndexPagesUiEvent.OnBack -> {
                    navigator.pop()
                }

                is IndexPagesUiEvent.OnBookClick -> {
                    val url = event.book.title.link
                    val id = url.substringAfterLast("/card").removeSuffix(".html")
                        .padStart(6, '0')
                    val groupId = url.substringAfterLast("/cards/").substringBefore("/")
                        .padStart(6, '0')
                    Napier.d(tag = TAG) { "goto url $url groupId $groupId id $id" }
                    navigator.goTo(
                        BookCardScreen(
                            bookCardId = id,
                            groupId = groupId,
                        ),
                    )
                }
            }
        }
    }
}

@Stable
data class IndexPagesState(
    val kanaLabel: String,
    val pagingData: LazyPagingItems<BookColumnItem>,
    val evenSink: (IndexPagesUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface IndexPagesUiEvent {
    data object OnBack : IndexPagesUiEvent

    data class OnBookClick(val book: BookColumnItem) : IndexPagesUiEvent
}
