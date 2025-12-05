/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.overlay

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.RootNavigator
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.OnGoToAppStore
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.dialog.ReaderCompleteDialogId
import me.andannn.aozora.ui.common.dialog.ReaderSettingDialogId
import me.andannn.aozora.ui.common.retainPresenter
import me.andannn.aozora.ui.common.util.SystemUiVisibilityEffect
import me.andannn.aozora.ui.feature.reader.viewer.BookPageState
import me.andannn.platform.Platform
import me.andannn.platform.platform
import org.koin.mp.KoinPlatform.getKoin

private const val TAG = "ReaderOverlayPresenter"

@Composable
internal fun retainReaderOverlayPresenter(
    cardId: String,
    authorId: String,
    bookPageState: BookPageState,
    settingRepository: UserDataRepository = getKoin().get(),
    navigator: Navigator = RootNavigator.current,
    popupController: PopupController = LocalPopupController.current,
    uriHandler: UriHandler = LocalUriHandler.current,
) = retainPresenter(cardId, authorId, settingRepository, bookPageState, popupController, navigator, uriHandler) {
    ReaderOverlayPresenter(
        cardId,
        authorId,
        navigator,
        settingRepository,
        bookPageState,
        popupController,
        uriHandler,
    )
}

private class ReaderOverlayPresenter(
    private val cardId: String,
    private val authorId: String,
    private val navigator: Navigator,
    private val userDataRepository: UserDataRepository,
    private val bookPageState: BookPageState,
    private val popupController: PopupController,
    private val uriHandler: UriHandler,
) : RetainedPresenter<ReaderOverlayState>() {
    val userMarkCompletedFlow =
        userDataRepository
            .isUserMarkCompletedFlow(cardId)
            .stateIn(
                retainedScope,
                started =
                    SharingStarted
                        .WhileSubscribed(1000),
                false,
            )
    val savedBookCardFlow =
        userDataRepository
            .getSavedBookById(bookId = cardId, authorId = authorId)
            .stateIn(
                retainedScope,
                started =
                    SharingStarted
                        .WhileSubscribed(1000),
                null,
            )

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun present(): ReaderOverlayState {
        var showOverlay by rememberSaveable {
            mutableStateOf(false)
        }
        val userMarkCompleted by userMarkCompletedFlow.collectAsStateWithLifecycle()
        val isLastPage by rememberUpdatedState(
            bookPageState.pagerState.currentPage == bookPageState.pagerState.pageCount - 1,
        )
        val savedBookCard by savedBookCardFlow.collectAsStateWithLifecycle()
        val isAddedToShelf by rememberUpdatedState(savedBookCard != null)

        suspend fun markCompletedAndShowAlertDialog() {
            userDataRepository.markBookAsCompleted(cardId, authorId)
            val result = popupController.showDialog(ReaderCompleteDialogId)
            if (result is OnGoToAppStore) {
                val url =
                    if (platform == Platform.ANDROID) {
                        "https://play.google.com/store/apps/details?id=me.andannn.aozora"
                    } else {
                        "https://apps.apple.com/app/id6746423917"
                    }
                uriHandler.openUri(url)
                navigator.pop()
            } else {
                navigator.pop()
            }
        }

        NavigationBackHandler(
            state = rememberNavigationEventState(NavigationEventInfo.None),
            isBackEnabled = isAddedToShelf && isLastPage && !userMarkCompleted,
        ) {
            retainedScope.launch {
                markCompletedAndShowAlertDialog()
            }
        }

        SystemUiVisibilityEffect(visible = showOverlay)

        return ReaderOverlayState(
            pagerState = bookPageState.pagerState,
            showOverlay = showOverlay,
        ) { event ->
            Napier.d(tag = TAG) { "on event $event" }
            when (event) {
                ReaderOverlayEvent.OnOpenFontSetting -> {
                    showOverlay = false
                    retainedScope.launch {
                        popupController.showDialog(ReaderSettingDialogId)
                    }
                }

                ReaderOverlayEvent.OnOpenTableOfContents -> {
                    showOverlay = false
                }

                ReaderOverlayEvent.OnToggleOverlay -> {
                    showOverlay = !showOverlay
                }

                ReaderOverlayEvent.OnBack -> {
                    retainedScope.launch {
                        if (isAddedToShelf && isLastPage && !userMarkCompleted) {
                            markCompletedAndShowAlertDialog()
                        } else {
                            navigator.pop()
                        }
                    }
                }
            }
        }
    }
}

data class ReaderOverlayState(
    val pagerState: PagerState,
    val showOverlay: Boolean,
    val eventSink: (ReaderOverlayEvent) -> Unit = {},
)

sealed interface ReaderOverlayEvent {
    data object OnOpenFontSetting : ReaderOverlayEvent

    data object OnOpenTableOfContents : ReaderOverlayEvent

    data object OnToggleOverlay : ReaderOverlayEvent

    data object OnBack : ReaderOverlayEvent
}
