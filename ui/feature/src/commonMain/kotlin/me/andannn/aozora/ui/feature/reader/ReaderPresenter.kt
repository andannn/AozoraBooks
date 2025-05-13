/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.BookModelTemp
import me.andannn.platform.PlatformAnalytics
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberReaderPresenter(
    cardId: String,
    userDataRepository: UserDataRepository = getKoin().get(),
    platformAnalytics: PlatformAnalytics = getKoin().get(),
) = remember(
    cardId,
    userDataRepository,
    platformAnalytics,
) {
    ReaderPresenter(cardId, userDataRepository, platformAnalytics)
}

class ReaderPresenter(
    private val cardId: String,
    private val userDataRepository: UserDataRepository,
    private val platformAnalytics: PlatformAnalytics,
) : Presenter<ReaderState> {
    init {
        platformAnalytics.logEvent("ScreenTransition", mapOf("screen" to "Reader"))
    }

    @Composable
    override fun present(): ReaderState {
        val savedBook by userDataRepository.getBookCache(cardId).collectAsRetainedState(null)
        return ReaderState(savedBook) { event ->
            when (event) {
                else -> {}
            }
        }
    }
}

@Stable
data class ReaderState(
    val bookCard: BookModelTemp?,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent
