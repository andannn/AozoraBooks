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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.Dp
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.exceptions.CopyRightRetainedException
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AozoraPage
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.LineSpacing
import me.andannn.aozora.core.domain.model.PageContext
import me.andannn.aozora.core.domain.model.ReadProgress
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.core.domain.model.TopMargin
import me.andannn.aozora.core.domain.pagesource.BookPageSource
import me.andannn.aozora.core.domain.pagesource.LocalBookPageSource
import me.andannn.aozora.core.domain.pagesource.PagerSnapShot
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.navigator.RootNavigator
import me.andannn.aozora.ui.common.widgets.rememberRefreshablePagerState
import me.andannn.aozora.ui.feature.common.dialog.OnAccept
import me.andannn.aozora.ui.feature.common.dialog.showAlertDialog
import me.andannn.platform.PlatformAnalytics
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberBookViewerPresenter(
    card: AozoraBookCard,
    screenWidthDp: Dp,
    screenHeightDp: Dp,
    bookSource: BookPageSource = LocalBookPageSource.current,
    popupController: PopupController = LocalPopupController.current,
    uriHandler: UriHandler = LocalUriHandler.current,
    navigator: Navigator = RootNavigator.current,
    settingRepository: UserDataRepository = getKoin().get(),
) = remember(card, bookSource, screenWidthDp, screenHeightDp, uriHandler, settingRepository) {
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

class BookViewerPresenter(
    private val card: AozoraBookCard,
    private val bookSource: BookPageSource,
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val userDataRepository: UserDataRepository,
    private val popupController: PopupController,
    private val navigator: Navigator,
    private val uriHandler: UriHandler,
) : Presenter<BookViewerState> {
    @Composable
    override fun present(): BookViewerState {
        val fontSize by userDataRepository
            .getFontSizeLevel()
            .collectAsRetainedState(FontSizeLevel.DEFAULT)
        val fontType by userDataRepository
            .getFontFontType()
            .collectAsRetainedState(FontType.DEFAULT)
        val theme by userDataRepository.getReaderTheme().collectAsRetainedState(ReaderTheme.DEFAULT)
        val topMargin by userDataRepository.getTopMargin().collectAsRetainedState(TopMargin.DEFAULT)
        val lineSpacing by userDataRepository
            .getLineSpacing()
            .collectAsRetainedState(LineSpacing.DEFAULT)
        val savedBookCard by userDataRepository
            .getSavedBookById(bookId = card.id, authorId = card.authorId)
            .collectAsRetainedState(null)
        val isAddedToShelf by rememberUpdatedState(savedBookCard != null)

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

        val density = LocalDensity.current
        val navigationBarHeightPx = WindowInsets.navigationBars.getBottom(density)
        val navigationBarHeight =
            rememberRetained {
                with(density) { navigationBarHeightPx.toDp() }
            }
        val statusBarHeightPx = WindowInsets.statusBars.getTop(density)
        val statusBarHeight =
            rememberRetained {
                with(density) { statusBarHeightPx.toDp() }
            }
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

                        is PagerSnapShot.Ready -> snapshotState = it
                    }
                }
        }
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
                else -> {}
            }
        }
    }
}

data class BookPageState(
    val pages: ImmutableList<AozoraPage>,
    val pagerState: PagerState,
)

data class BookViewerState(
    val fontType: FontType,
    val bookPageState: BookPageState,
    val theme: ReaderTheme = ReaderTheme.DYNAMIC,
    val evenSink: (BookViewerUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface BookViewerUiEvent
