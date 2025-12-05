/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.exceptions.CopyRightRetainedException
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AozoraPage
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.LineSpacing
import me.andannn.aozora.core.domain.model.PageContext
import me.andannn.aozora.core.domain.model.READ_PROGRESS_DONE
import me.andannn.aozora.core.domain.model.READ_PROGRESS_NONE
import me.andannn.aozora.core.domain.model.ReadProgress
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.core.domain.model.TopMargin
import me.andannn.aozora.core.domain.pagesource.BookPageSource
import me.andannn.aozora.core.domain.pagesource.LocalBookPageSource
import me.andannn.aozora.core.domain.pagesource.PagerSnapShot
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.RootNavigator
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.OnAccept
import me.andannn.aozora.ui.common.dialog.OnGoToAppStore
import me.andannn.aozora.ui.common.dialog.OnJumpTo
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.dialog.ReaderCompleteDialogId
import me.andannn.aozora.ui.common.dialog.ReaderSettingDialogId
import me.andannn.aozora.ui.common.dialog.TableOfContentsDialogId
import me.andannn.aozora.ui.common.dialog.showAlertDialog
import me.andannn.aozora.ui.common.retainPresenter
import me.andannn.aozora.ui.common.widgets.rememberRefreshablePagerState
import me.andannn.platform.Platform
import me.andannn.platform.PlatformAnalytics
import me.andannn.platform.platform
import org.koin.mp.KoinPlatform.getKoin

@Composable
internal fun retainBookViewerPresenter(
    card: AozoraBookCard,
    screenWidthDp: Dp,
    screenHeightDp: Dp,
    bookSource: BookPageSource = LocalBookPageSource.current,
    popupController: PopupController = LocalPopupController.current,
    uriHandler: UriHandler = LocalUriHandler.current,
    navigator: Navigator = RootNavigator.current,
    settingRepository: UserDataRepository = getKoin().get(),
) = retainPresenter(
    card,
    bookSource,
    screenWidthDp,
    screenHeightDp,
    uriHandler,
    settingRepository,
) {
    BookViewerPresenter(
        card = card,
        bookSource = bookSource,
        popupController = popupController,
        navigator = navigator,
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        uriHandler = uriHandler,
        userDataRepository = settingRepository,
    )
}

private const val TAG = "ReaderPresenter"

private class BookViewerPresenter(
    private val card: AozoraBookCard,
    private val bookSource: BookPageSource,
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val userDataRepository: UserDataRepository,
    private val popupController: PopupController,
    private val navigator: Navigator,
    private val uriHandler: UriHandler,
) : RetainedPresenter<BookViewerState>() {
    val fontSizeFlow =
        userDataRepository.getFontSizeLevel().stateIn(
            retainedScope,
            started = SharingStarted.WhileSubscribed(1000),
            FontSizeLevel.DEFAULT,
        )
    val fontTypeFlow =
        userDataRepository.getFontFontType().stateIn(
            retainedScope,
            started = SharingStarted.WhileSubscribed(1000),
            FontType.DEFAULT,
        )
    val topMarginFlow =
        userDataRepository.getTopMargin().stateIn(
            retainedScope,
            started = SharingStarted.WhileSubscribed(1000),
            TopMargin.DEFAULT,
        )
    val lineSpacingFlow =
        userDataRepository.getLineSpacing().stateIn(
            retainedScope,
            started = SharingStarted.WhileSubscribed(1000),
            LineSpacing.DEFAULT,
        )
    val themeFlow =
        userDataRepository.getReaderTheme().stateIn(
            retainedScope,
            started = SharingStarted.WhileSubscribed(1000),
            ReaderTheme.DEFAULT,
        )

    val savedBookCardFlow =
        userDataRepository
            .getSavedBookById(bookId = card.id, authorId = card.authorId)
            .stateIn(
                retainedScope,
                started = SharingStarted.WhileSubscribed(1000),
                null,
            )
    val userMarkCompletedFlow =
        userDataRepository
            .isUserMarkCompletedFlow(card.id)
            .stateIn(
                retainedScope,
                started = SharingStarted.WhileSubscribed(1000),
                false,
            )

    @Composable
    override fun present(): BookViewerState {
        val fontSize by fontSizeFlow.collectAsStateWithLifecycle()
        val fontType by fontTypeFlow.collectAsStateWithLifecycle()
        val topMargin by topMarginFlow.collectAsStateWithLifecycle()
        val lineSpacing by lineSpacingFlow.collectAsStateWithLifecycle()
        val theme by themeFlow.collectAsStateWithLifecycle()
        val savedBookCard by savedBookCardFlow.collectAsStateWithLifecycle(null)
        val isAddedToShelf by rememberUpdatedState(savedBookCard != null)
        val userMarkCompleted by userMarkCompletedFlow.collectAsStateWithLifecycle()

        var snapshotState by remember {
            mutableStateOf<PagerSnapShot.Ready?>(null)
        }

        val pagerState =
            rememberRefreshablePagerState(
                initialPage = snapshotState?.initialIndex ?: 0,
                version = snapshotState?.snapshotVersion,
            ) {
                snapshotState?.pageList?.size ?: 0
            }
        val isLastPage by rememberUpdatedState(
            pagerState.currentPage == pagerState.pageCount - 1,
        )

        val density = LocalDensity.current
        val navigationBarHeightPx = WindowInsets.navigationBars.getBottom(density)
        val navigationBarHeight =
            with(density) { navigationBarHeightPx.toDp() }
        val statusBarHeightPx = WindowInsets.statusBars.getTop(density)
        val statusBarHeight =
            with(density) { statusBarHeightPx.toDp() }
        // update progress when page changed.
        LaunchedEffect(
            snapshotState?.snapshotVersion,
            isAddedToShelf,
        ) {
            if (!isAddedToShelf) {
                Napier.d(tag = TAG) { "book not added to shelf, skip" }
                return@LaunchedEffect
            }

            Napier.d(tag = TAG) { "start observing page change event." }
            val totalCount = bookSource.getTotalBlockCount() ?: return@LaunchedEffect
            snapshotFlow { pagerState.settledPage }
                .drop(1)
                .collect { newIndex ->
                    Napier.d(tag = TAG) { "new settled page collected $newIndex" }
                    val page = snapshotState?.pageList?.get(newIndex)
                    if (page != null) {
                        val currentProgress =
                            when (page) {
                                is AozoraPage.AozoraCoverPage -> {
                                    ReadProgress.None
                                }

                                is AozoraPage.AozoraBibliographicalPage -> {
                                    ReadProgress.Done
                                }

                                is AozoraPage.AozoraRoughPage -> {
                                    ReadProgress.Reading(
                                        blockIndex = page.pageProgress.first,
                                        totalBlockCount = totalCount,
                                    )
                                }
                            }
                        userDataRepository.setProgressOfBook(
                            bookCardId = card.id,
                            authorId = card.authorId,
                            readProgress = currentProgress,
                        )
                    }
                }
        }

        LaunchedEffect(
            fontSize,
            topMargin,
            lineSpacing,
        ) {
            val pageMetadata =
                PageContext(
                    navigationBarHeight = navigationBarHeight,
                    statusBarHeight = statusBarHeight,
                    originalHeight = screenHeightDp,
                    originalWidth = screenWidthDp,
                    additionalTopMargin = topMargin,
                    fontSizeLevel = fontSize,
                    fontType = fontType,
                    lineSpacing = lineSpacing,
                )
            val savedProgress = userDataRepository.getProgress(card.id)
            bookSource
                .getPagerSnapShotFlow(pageMetadata, readingProgress = savedProgress)
                .distinctUntilChanged()
                .collect {
                    when (it) {
                        is PagerSnapShot.Error -> {
                            Napier.e(tag = TAG) { "msg: ${it.exception.message} cause: ${it.exception.cause}" }

                            // report error to analytics.
                            launch {
                                getKoin()
                                    .get<PlatformAnalytics>()
                                    .recordException(it.exception)
                            }

                            val result = popupController.showAlertDialog(it.exception)
                            if (result == OnAccept && it.exception is CopyRightRetainedException) {
                                uriHandler.openUri((it.exception as CopyRightRetainedException).bookCard.cardUrl)
                            }

                            // dialog closed.
                            navigator.pop()
                        }

                        is PagerSnapShot.Ready -> {
                            snapshotState = it
                        }
                    }
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

        val uiScope = rememberCoroutineScope()
        return BookViewerState(
            fontType = fontType,
            theme = theme,
            bookPageState =
                BookPageState(
                    pages = snapshotState?.pageList ?: emptyList<AozoraPage>().toImmutableList(),
                    pagerState = pagerState,
                ),
        ) { eventSink ->
            when (eventSink) {
                BookViewerUiEvent.OnShowTableOfContentDialog -> {
                    retainedScope.launch {
                        val tableOfContents = bookSource.getTableOfContents()
                        val result =
                            popupController.showDialog(TableOfContentsDialogId(tableOfContents))
                        Napier.d(tag = TAG) { "on jump to result $result" }
                        if (result is OnJumpTo) {
                            uiScope.onJumpTo(
                                pages =
                                    snapshotState?.pageList
                                        ?: emptyList<AozoraPage>().toImmutableList(),
                                pagerState = pagerState,
                                blockIndex = result.blockIndex,
                            )
                        }
                    }
                }

                BookViewerUiEvent.OnShowSettingDialog -> {
                    retainedScope.launch {
                        popupController.showDialog(ReaderSettingDialogId)
                    }
                }

                BookViewerUiEvent.OnBack -> {
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

    suspend fun markCompletedAndShowAlertDialog() {
        userDataRepository.markBookAsCompleted(card.id, card.authorId)
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
}

private fun CoroutineScope.onJumpTo(
    pagerState: PagerState,
    blockIndex: Int,
    pages: ImmutableList<AozoraPage>,
) {
    val pageIndex =
        pages.indexOfFirst { page ->
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
        launch {
            pagerState.animateScrollToPage(pageIndex)
        }
    }
}

internal data class BookPageState(
    val pages: ImmutableList<AozoraPage>,
    val pagerState: PagerState,
)

internal data class BookViewerState(
    val fontType: FontType,
    val bookPageState: BookPageState,
    val theme: ReaderTheme = ReaderTheme.DYNAMIC,
    val evenSink: (BookViewerUiEvent) -> Unit = {},
)

internal sealed interface BookViewerUiEvent {
    data object OnShowTableOfContentDialog : BookViewerUiEvent

    data object OnShowSettingDialog : BookViewerUiEvent

    data object OnBack : BookViewerUiEvent
}
