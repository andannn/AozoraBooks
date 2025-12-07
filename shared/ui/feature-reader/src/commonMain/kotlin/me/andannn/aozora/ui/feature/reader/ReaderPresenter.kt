/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.pagesource.BookPageSource
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.retainPresenter
import me.andannn.aozora.ui.common.util.ImmersiveModeEffect
import me.andannn.aozora.ui.common.util.KeepScreenOnEffect
import org.koin.mp.KoinPlatform.getKoin

@Composable
internal fun retainReaderPresenter(
    cardId: String,
    authorId: String,
    userDataRepository: UserDataRepository = getKoin().get(),
) = retainPresenter(
    cardId,
    userDataRepository,
) {
    ReaderPresenter(cardId, authorId, userDataRepository)
}

private class ReaderPresenter(
    private val cardId: String,
    private val authorId: String,
    private val userDataRepository: UserDataRepository,
) : RetainedPresenter<ReaderState>() {
    val savedBookFlow = MutableStateFlow<AozoraBookCard?>(null)
    val pageSourceFlow = MutableStateFlow<BookPageSource?>(null)

    init {
        retainedScope.launch {
            val bookCache =
                userDataRepository.getBookCache(cardId, authorId).first() ?: return@launch

            savedBookFlow.value = bookCache

            pageSourceFlow.value =
                getKoin()
                    .get<BookPageSource.Factory>()
//                    .createDummySource()
                    .createBookPageSource(bookCache, retainedScope)
        }
    }

    @Composable
    override fun present(): ReaderState {
        val savedBook by savedBookFlow.collectAsStateWithLifecycle()
        val pageSource by pageSourceFlow.collectAsStateWithLifecycle()

        KeepScreenOnEffect()
        ImmersiveModeEffect()

        return ReaderState(savedBook, pageSource) { event ->
            when (event) {
                else -> {}
            }
        }
    }

    override fun onClear() {
        pageSourceFlow.value?.close()
    }
}

@Stable
data class ReaderState(
    val bookCard: AozoraBookCard?,
    val bookPageSource: BookPageSource?,
    val evenSink: (ReaderUiEvent) -> Unit = {},
)

sealed interface ReaderUiEvent
