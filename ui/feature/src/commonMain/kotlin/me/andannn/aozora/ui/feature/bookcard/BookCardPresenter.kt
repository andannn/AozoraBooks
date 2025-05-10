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
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.data.AozoraContentsRepository
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberBookCardPresenter(
    groupId: String,
    bookId: String,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = remember(
    groupId,
    bookId,
    aozoraContentsRepository,
    navigator,
) {
    BookCardPresenter(
        groupId = groupId,
        bookId = bookId,
        aozoraContentsRepository = aozoraContentsRepository,
        navigator = navigator,
    )
}

private const val TAG = "BookCardPresenter"

class BookCardPresenter(
    private val groupId: String,
    private val bookId: String,
    private val aozoraContentsRepository: AozoraContentsRepository,
    private val navigator: Navigator,
) : Presenter<BookCardState> {
    @Composable
    override fun present(): BookCardState {
        var bookCardInfo by rememberRetained {
            mutableStateOf<AozoraBookCard?>(null)
        }

        LaunchedEffect(Unit) {
            Napier.d(tag = TAG) { "present groupId $groupId, bookId $bookId" }
            val result = aozoraContentsRepository.getBookCard(cardId = bookId, groupId = groupId)
            bookCardInfo = result
        }
        return BookCardState(
            bookCardInfo = bookCardInfo,
        ) { event ->
            when (event) {
                BookCardUiEvent.Back -> navigator.pop()
            }
        }
    }
}

@Stable
data class BookCardState(
    val bookCardInfo: AozoraBookCard? = null,
    val evenSink: (BookCardUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface BookCardUiEvent {
    data object Back : BookCardUiEvent
}
