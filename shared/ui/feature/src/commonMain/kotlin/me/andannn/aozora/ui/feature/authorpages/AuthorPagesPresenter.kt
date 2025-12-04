/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.authorpages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import io.github.andannn.RetainedModel
import io.github.andannn.retainRetainedModel
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.common.screens.AuthorScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun retainAuthorPagesPresenter(
    code: String,
    aozoraRepository: AozoraContentsRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = retainRetainedModel(
    code,
    aozoraRepository,
    navigator,
) {
    AuthorPagesPresenter(KanaLineItem.fromCode(code) ?: error("Never"), aozoraRepository, navigator)
}

private const val TAG = "AuthorPagesPresenter"

class AuthorPagesPresenter(
    private val kanaLineItem: KanaLineItem,
    private val aozoraRepository: AozoraContentsRepository,
    private val navigator: Navigator,
) : RetainedModel(),
    Presenter<AuthorPagesState> {
    val pagingDataFlow =
        aozoraRepository.getAuthorsPagingFlow(kanaLineItem).cachedIn(retainedScope)

    @Composable
    override fun present(): AuthorPagesState {
        Napier.d(tag = TAG) { "present $kanaLineItem" }
        val pagingData = pagingDataFlow.collectAsLazyPagingItems()
        return AuthorPagesState(
            kanaLineItem,
            pagingData,
        ) { event ->
            when (event) {
                AuthorPagesUiEvent.OnBack -> {
                    navigator.pop()
                }

                is AuthorPagesUiEvent.OnAuthorClick -> {
                    navigator.goTo(AuthorScreen(event.author.authorId))
                }
            }
        }
    }
}

@Stable
data class AuthorPagesState(
    val kanaLineItem: KanaLineItem,
    val pagingData: LazyPagingItems<AuthorData>,
    val evenSink: (AuthorPagesUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface AuthorPagesUiEvent {
    data object OnBack : AuthorPagesUiEvent

    data class OnAuthorClick(
        val author: AuthorData,
    ) : AuthorPagesUiEvent
}
