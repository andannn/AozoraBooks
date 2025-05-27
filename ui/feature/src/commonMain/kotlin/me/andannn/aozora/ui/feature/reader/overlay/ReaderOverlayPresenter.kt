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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraPage
import me.andannn.aozora.core.domain.model.READ_PROGRESS_DONE
import me.andannn.aozora.core.domain.model.READ_PROGRESS_NONE
import me.andannn.aozora.core.domain.model.ReadProgress
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.common.util.SystemUiVisibilityEffect
import me.andannn.aozora.ui.feature.dialog.OnGoToAppStore
import me.andannn.aozora.ui.feature.dialog.OnJumpTo
import me.andannn.aozora.ui.feature.dialog.ReaderCompleteDialogId
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
    uriHandler: UriHandler = LocalUriHandler.current,
) = remember(bookPageState, popupController, navigator, uriHandler) {
    ReaderOverlayPresenter(
        cardId,
        navigator,
        settingRepository,
        bookPageState,
        popupController,
        uriHandler,
    )
}

class ReaderOverlayPresenter(
    private val cardId: String,
    private val navigator: Navigator,
    private val userDataRepository: UserDataRepository,
    private val bookPageState: BookPageState,
    private val popupController: PopupController,
    private val uriHandler: UriHandler,
) : Presenter<ReaderOverlayState> {
    @Composable
    override fun present(): ReaderOverlayState {
        var showOverlay by rememberSaveable {
            mutableStateOf(false)
        }
        val progress by userDataRepository
            .getProgressFlow(cardId)
            .collectAsRetainedState(ReadProgress.None)

        val scope = rememberCoroutineScope()
        val userMarkCompleted by userDataRepository
            .isUserMarkCompletedFlow(cardId)
            .collectAsRetainedState(false)
        val isLastPage by rememberUpdatedState(
            bookPageState.pagerState.currentPage == bookPageState.pagerState.pageCount - 1,
        )

        suspend fun markCompletedAndShowAlertDialog() {
            userDataRepository.markBookAsCompleted(cardId)
            val result = popupController.showDialog(ReaderCompleteDialogId)
            if (result is OnGoToAppStore) {
                uriHandler.openUri("https://play.google.com/store/apps/details?id=me.andannn.aozora")
                navigator.pop()
            } else {
                navigator.pop()
            }
        }

        BackHandler(enabled = isLastPage && !userMarkCompleted) {
            Napier.d(tag = TAG) { "back pressed when book completed" }
            scope.launch {
                markCompletedAndShowAlertDialog()
            }
        }

        SystemUiVisibilityEffect(visible = showOverlay)

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
                    scope.launch {
                        if (isLastPage && !userMarkCompleted) {
                            markCompletedAndShowAlertDialog()
                        } else {
                            navigator.pop()
                        }
                    }
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
