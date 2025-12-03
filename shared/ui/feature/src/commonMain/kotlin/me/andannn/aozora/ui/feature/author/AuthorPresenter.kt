/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.author

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AuthorWithBooks
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.common.screens.BookCardScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberAuthorPresenter(
    authorId: String,
    aozoraRepository: AozoraContentsRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = remember(
    authorId,
    aozoraRepository,
    navigator,
) {
    AuthorPresenter(authorId, aozoraRepository, navigator)
}

private const val TAG = "AuthorPresenter"

class AuthorPresenter(
    private val authorId: String,
    private val aozoraRepository: AozoraContentsRepository,
    private val navigator: Navigator,
) : Presenter<AuthorState> {
    @Composable
    override fun present(): AuthorState {
        Napier.d(tag = TAG) { "present $authorId" }
        val authorData by
            aozoraRepository.getAuthorDataWithBooks(authorId).collectAsRetainedState(null)
        return AuthorState(
            authorId = authorId,
            authorData = authorData,
        ) { event ->
            when (event) {
                AuthorUiEvent.OnBack -> {
                    navigator.pop()
                }

                is AuthorUiEvent.OnBookClick -> {
                    navigator.goTo(BookCardScreen(bookCardId = event.book.id, groupId = authorId))
                }
            }
        }
    }
}

@Stable
data class AuthorState(
    val authorId: String,
    val authorData: AuthorWithBooks?,
    val evenSink: (AuthorUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface AuthorUiEvent {
    data object OnBack : AuthorUiEvent

    data class OnBookClick(
        val book: AozoraBookCard,
    ) : AuthorUiEvent
}
