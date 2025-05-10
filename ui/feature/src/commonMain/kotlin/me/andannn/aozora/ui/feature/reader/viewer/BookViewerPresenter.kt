/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Size
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.BookPreviewInfo
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.PageContext
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.data.common.TopMargin
import me.andannn.aozora.core.pagesource.BookPageSource
import me.andannn.aozora.core.pagesource.LocalBookPageSource
import me.andannn.aozora.core.pagesource.PagerSnapShot
import me.andannn.aozora.ui.common.widgets.rememberRefreshablePagerState
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberBookViewerPresenter(
    card: BookPreviewInfo,
    screenSize: Size,
    bookSource: BookPageSource = LocalBookPageSource.current,
    settingRepository: UserDataRepository = getKoin().get(),
) = remember(card, bookSource, screenSize, settingRepository) {
    BookViewerPresenter(
        card,
        bookSource,
        screenSize,
        settingRepository,
    )
}

private const val TAG = "ReaderPresenter"

class BookViewerPresenter(
    private val card: BookPreviewInfo,
    private val bookSource: BookPageSource,
    private val screenSize: Size,
    private val settingRepository: UserDataRepository,
) : Presenter<BookViewerState> {
    @Composable
    override fun present(): BookViewerState {
        val fontSize by settingRepository
            .getFontSizeLevel()
            .collectAsRetainedState(FontSizeLevel.DEFAULT)
        val fontType by settingRepository.getFontFontType().collectAsRetainedState(FontType.DEFAULT)
        val theme by settingRepository.getReaderTheme().collectAsRetainedState(ReaderTheme.DEFAULT)
        val topMargin by settingRepository.getTopMargin().collectAsRetainedState(TopMargin.DEFAULT)
        val lineSpacing by settingRepository
            .getLineSpacing()
            .collectAsRetainedState(LineSpacing.DEFAULT)

        var snapshotState by remember {
            mutableStateOf<PagerSnapShot?>(null)
        }

        val pagerState =
            rememberRefreshablePagerState(
                initialPage = snapshotState?.initialIndex ?: 0,
                version = snapshotState?.snapshotVersion,
            ) {
                snapshotState?.pageList?.size ?: 0
            }

        LaunchedEffect(
            snapshotState?.snapshotVersion,
        ) {
            Napier.d(tag = TAG) { "invoked ${snapshotState?.snapshotVersion}" }
            snapshotFlow { pagerState.settledPage }
                .drop(1)
                .collect { newIndex ->
                    Napier.d(tag = TAG) { "new settled page collected $newIndex" }
                    val page = snapshotState?.pageList?.get(newIndex)

                    if (page != null) {
                        settingRepository.setProgressOfBook(
                            bookCardId = card.id,
                            blockIndex = (page as? AozoraPage.AozoraRoughPage)?.pageProgress?.first,
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
                    originalHeight = screenSize.height,
                    originalWidth = screenSize.width,
                    additionalTopMargin = topMargin,
                    fontSizeLevel = fontSize,
                    fontType = fontType,
                    lineSpacing = lineSpacing,
                )
            val savedBlockIndex = settingRepository.getProgress(card.id)
            bookSource
                .getPagerSnapShotFlow(pageMetadata, initialBlockIndex = savedBlockIndex?.toInt())
                .distinctUntilChanged()
                .collect {
                    snapshotState = it
                }
        }
        return BookViewerState(
            pageMetadata =
                PageContext(
                    originalHeight = screenSize.height,
                    originalWidth = screenSize.width,
                    additionalTopMargin = topMargin,
                    fontSizeLevel = fontSize,
                    fontType = fontType,
                    lineSpacing = lineSpacing,
                ),
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
    val pageMetadata: PageMetaData,
    val bookPageState: BookPageState,
    val theme: ReaderTheme = ReaderTheme.DYNAMIC,
    val evenSink: (BookViewerUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface BookViewerUiEvent
