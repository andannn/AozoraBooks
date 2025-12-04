/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import io.github.andannn.RetainedModel
import io.github.andannn.retainRetainedModel
import kotlinx.coroutines.flow.stateIn
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.util.ImmersiveModeEffect
import me.andannn.aozora.ui.common.util.KeepScreenOnEffect
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun retainReaderPresenter(
    cardId: String,
    authorId: String,
    userDataRepository: UserDataRepository = getKoin().get(),
) = retainRetainedModel(
    cardId,
    userDataRepository,
) {
    ReaderPresenter(cardId, authorId, userDataRepository)
}

class ReaderPresenter(
    private val cardId: String,
    private val authorId: String,
    private val userDataRepository: UserDataRepository,
) : RetainedModel(),
    Presenter<ReaderState> {
    val savedBookFlow =
        userDataRepository
            .getBookCache(cardId, authorId)
            .stateIn(
                retainedScope,
                initialValue = null,
                started =
                    kotlinx.coroutines.flow.SharingStarted
                        .WhileSubscribed(5000),
            )

    @Composable
    override fun present(): ReaderState {
        val savedBook by savedBookFlow.collectAsStateWithLifecycle()

        KeepScreenOnEffect()
        ImmersiveModeEffect()

        return ReaderState(savedBook) { event ->
            when (event) {
                else -> {}
            }
        }
    }
}

@Stable
data class ReaderState(
    val bookCard: AozoraBookCard?,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent
