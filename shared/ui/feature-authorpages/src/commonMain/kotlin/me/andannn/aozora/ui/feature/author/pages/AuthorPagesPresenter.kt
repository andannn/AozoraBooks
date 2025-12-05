/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.author.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.AuthorScreen
import me.andannn.aozora.ui.common.LocalNavigator
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.retainPresenter
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun retainAuthorPagesPresenter(
    code: String,
    aozoraRepository: AozoraContentsRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = retainPresenter(
    code,
    aozoraRepository,
    navigator,
) {
    AuthorPagesPresenter(KanaLineItem.fromCode(code) ?: error("Never"), aozoraRepository, navigator)
}

@Stable
data class AuthorPagesState(
    val kanaLineItem: KanaLineItem,
    val pagingData: LazyPagingItems<AuthorData>,
    val evenSink: (AuthorPagesUiEvent) -> Unit = {},
)

sealed interface AuthorPagesUiEvent {
    data object OnBack : AuthorPagesUiEvent

    data class OnAuthorClick(
        val author: AuthorData,
    ) : AuthorPagesUiEvent
}

private const val TAG = "AuthorPagesPresenter"

private class AuthorPagesPresenter(
    private val kanaLineItem: KanaLineItem,
    private val aozoraRepository: AozoraContentsRepository,
    private val navigator: Navigator,
) : RetainedPresenter<AuthorPagesState>() {
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
