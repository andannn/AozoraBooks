/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.overlay

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.READ_PROGRESS_DONE
import me.andannn.aozora.core.data.common.READ_PROGRESS_NONE
import me.andannn.aozora.core.data.common.ReadProgress
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.dialog.OnJumpTo
import me.andannn.aozora.ui.feature.dialog.ReaderSettingDialogId
import me.andannn.aozora.ui.feature.dialog.TableOfContentsDialogId
import me.andannn.aozora.ui.feature.reader.viewer.BookPageState
import org.koin.mp.KoinPlatform.getKoin

private const val TAG = "ReaderOverlayPresenter"

@Composable
fun rememberReaderOverlayPresenter(
    cardId: String,
    bookPageState: BookPageState,
    settingRepository: UserDataRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
    popupController: PopupController = LocalPopupController.current,
) = remember(bookPageState, popupController, navigator) {
    ReaderOverlayPresenter(cardId, navigator, settingRepository, bookPageState, popupController)
}

class ReaderOverlayPresenter(
    private val cardId: String,
    private val navigator: Navigator,
    private val settingRepository: UserDataRepository,
    private val bookPageState: BookPageState,
    private val popupController: PopupController,
) : Presenter<ReaderOverlayState> {
    @Composable
    override fun present(): ReaderOverlayState {
        val scope = rememberCoroutineScope()
        var showOverlay by remember {
            mutableStateOf(false)
        }
        val progress by settingRepository
            .getProgressFlow(cardId)
            .collectAsRetainedState(ReadProgress.None)

        return ReaderOverlayState(
            progress = progress,
            pagerState = bookPageState.pagerState,
            showOverlay = showOverlay,
        ) { event ->
            Napier.d(tag = TAG) { "on event $event" }
            when (event) {
                ReaderOverlayEvent.OnOpenFontSetting -> {
                    showOverlay = false
                    scope.launch {
                        popupController.showDialog(ReaderSettingDialogId)
                    }
                }

                ReaderOverlayEvent.OnOpenTableOfContents -> {
                    showOverlay = false
                    scope.launch {
                        val result = popupController.showDialog(TableOfContentsDialogId)
                        Napier.d(tag = TAG) { "on jump to result $result" }
                        if (result is OnJumpTo) {
                            onJumpTo(result.blockIndex)
                        }
                    }
                }

                ReaderOverlayEvent.OnToggleOverlay -> {
                    showOverlay = !showOverlay
                }

                ReaderOverlayEvent.OnBack -> {
                    navigator.pop()
                }
            }
        }
    }

    private suspend fun onJumpTo(blockIndex: Int) {
        val pageIndex =
            bookPageState.pages.indexOfFirst { page ->
                when (page) {
                    is AozoraPage.AozoraBibliographicalPage -> {
                        blockIndex == READ_PROGRESS_DONE
                    }

                    is AozoraPage.AozoraCoverPage -> {
                        blockIndex == READ_PROGRESS_NONE
                    }

                    is AozoraPage.AozoraRoughPage -> {
                        blockIndex in page.pageProgress
                    }
                }
            }
        if (pageIndex != -1) {
            bookPageState.pagerState.animateScrollToPage(pageIndex)
        }
    }
}

data class ReaderOverlayState(
    val progress: ReadProgress,
    val pagerState: PagerState,
    val showOverlay: Boolean,
    val eventSink: (ReaderOverlayEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderOverlayEvent {
    data object OnOpenFontSetting : ReaderOverlayEvent

    data object OnOpenTableOfContents : ReaderOverlayEvent

    data object OnToggleOverlay : ReaderOverlayEvent

    data object OnBack : ReaderOverlayEvent
}
