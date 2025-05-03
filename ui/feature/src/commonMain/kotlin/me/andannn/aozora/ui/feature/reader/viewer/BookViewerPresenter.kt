package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.distinctUntilChanged
import me.andannn.aozora.core.data.UserSettingRepository
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.PageContext
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.pagesource.BookPageSource
import me.andannn.aozora.core.pagesource.LayoutPageSource
import me.andannn.aozora.core.pagesource.PagerSnapShot
import me.andannn.aozora.ui.common.widgets.rememberRefreshablePagerState
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberBookViewerPresenter(
    card: AozoraBookCard,
    screenSize: Size,
    initialProgress: Long,
    settingRepository: UserSettingRepository = getKoin().get(),
) = remember(card, initialProgress, screenSize, settingRepository) {
    BookViewerPresenter(
        card,
        initialProgress,
        screenSize,
        settingRepository,
    )
}

private const val TAG = "ReaderPresenter"

class BookViewerPresenter(
    private val card: AozoraBookCard,
    private val initialProgress: Long,
    private val screenSize: Size,
    private val settingRepository: UserSettingRepository,
) : Presenter<BookViewerState> {
    @Composable
    override fun present(): BookViewerState {
        val fontSize by settingRepository.getFontSizeLevel().collectAsRetainedState()
        val fontType by settingRepository.getFontFontType().collectAsRetainedState()
        val theme by settingRepository.getReaderTheme().collectAsRetainedState()
        val topMargin by settingRepository.getTopMargin().collectAsRetainedState()
        val lineSpacing by settingRepository.getLineSpacing().collectAsRetainedState()

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
//
//        var settledPageState by remember {
//            mutableStateOf<AozoraPage?>(null)
//        }
//
//        val settledPage by rememberUpdatedState(pagerState.settledPage)
//
//        LaunchedEffect(Unit) {
//            var lastKnownVersion: Int = 0
//
//            combine(
//                snapshotFlow { settledPage },
//                snapshotFlow { snapshotState }.filterNotNull(),
//            ) { settledPageIndex, snapShot -> settledPageIndex to snapShot }
//                .collect { (settledPageIndex, snapShot) ->
//                    val snapShotVersion = snapShot.snapshotVersion
//                    if (snapShotVersion == 0) {
//                        settledPageState = snapShot.pageList.getOrNull(settledPageIndex)
//                        return@collect
//                    }
//
//                    val changedByPageList = (lastKnownVersion != snapShotVersion)
//                    lastKnownVersion = snapShotVersion
//
//                    if (changedByPageList) {
//                        Napier.d(tag = TAG) { "SettledPage changed by pageList." }
//                    } else {
//                        Napier.d(tag = TAG) { "SettledPage changed by user gesture." }
//                        settledPageState = snapShot.pageList.getOrNull(settledPageIndex)
//                    }
//                }
//        }
//
//        val settledPageFlow = remember {
//            snapshotFlow { settledPageState }.distinctUntilChanged()
//        }

        val scope = rememberCoroutineScope()
        val bookSource: BookPageSource =
            remember {
                LayoutPageSource(
                    card,
                    scope = scope,
                )
            }
        DisposableEffect(Unit) {
            onDispose {
                bookSource.dispose()
            }
        }

        LaunchedEffect(
            initialProgress,
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
            bookSource
                .getPagerSnapShotFlow(pageMetadata, initialProgress = initialProgress)
                .distinctUntilChanged()
                .collect {
                    Napier.d(tag = TAG) {
                        "present: New snapshot emit. version ${it.snapshotVersion} currentIndex ${it.initialIndex}, size ${
                            it.pageList.map {
                                it
                                    .hashCode()
                            }
                        }"
                    }
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
            pages = snapshotState?.pageList ?: emptyList(),
            pagerState = pagerState,
        ) { eventSink ->
            when (eventSink) {
                else -> {}
            }
        }
    }
}

@Stable
data class BookViewerState(
    val pageMetadata: PageMetaData,
    val pages: List<AozoraPage>,
    val theme: ReaderTheme = ReaderTheme.DYNAMIC,
    val pagerState: PagerState,
    val evenSink: (BookViewerUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface BookViewerUiEvent
