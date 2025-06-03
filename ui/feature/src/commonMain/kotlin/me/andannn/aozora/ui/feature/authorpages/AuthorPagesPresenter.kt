/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.authorpages

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
import me.andannn.aozora.core.domain.model.AuthorModel
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.core.util.rememberRetainedCoroutineScope
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberAuthorPagesPresenter(
    code: String,
    aozoraRepository: AozoraContentsRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = remember(
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
) : Presenter<AuthorPagesState> {
    @Composable
    override fun present(): AuthorPagesState {
        Napier.d(tag = TAG) { "present $kanaLineItem" }
        val scope = rememberRetainedCoroutineScope()
        val pagingDataFlow =
            rememberRetained {
                aozoraRepository.getAuthorsPagingFlow(kanaLineItem).cachedIn(scope)
            }
        val pagingData = pagingDataFlow.collectAsLazyPagingItems()
        return AuthorPagesState(
            kanaLineItem,
            pagingData,
        ) { event ->
            when (event) {
                AuthorPagesUiEvent.OnBack -> {
                    navigator.pop()
                }

                is AuthorPagesUiEvent.OnAuthorClick -> TODO()
            }
        }
    }
}

@Stable
data class AuthorPagesState(
    val kanaLineItem: KanaLineItem,
    val pagingData: LazyPagingItems<AuthorModel>,
    val evenSink: (AuthorPagesUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface AuthorPagesUiEvent {
    data object OnBack : AuthorPagesUiEvent

    data class OnAuthorClick(
        val author: AuthorModel,
    ) : AuthorPagesUiEvent
}
