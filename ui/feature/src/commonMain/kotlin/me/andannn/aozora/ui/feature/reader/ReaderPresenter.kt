/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.core.data.common.BookPreviewInfo
import me.andannn.aozora.ui.feature.home.library.bookCardList

@Composable
fun rememberReaderPresenter(cardId: String) =
    remember(
        cardId,
    ) {
        ReaderPresenter(cardId)
    }

class ReaderPresenter(
    private val cardId: String,
) : Presenter<ReaderState> {
    @Composable
    override fun present(): ReaderState {
        val scope = rememberCoroutineScope()
        return ReaderState(getCardById(cardId)) { event ->
            when (event) {
                else -> {}
            }
        }
    }
}

private fun getCardById(id: String): BookPreviewInfo = bookCardList.firstOrNull { it.id == id } ?: error("")

@Stable
data class ReaderState(
    val bookCard: BookPreviewInfo,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent
