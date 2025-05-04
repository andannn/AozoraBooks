package me.andannn.aozora.ui.feature.reader.viewer

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.PageContext
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.pagesource.BookPageSource
import me.andannn.aozora.core.pagesource.PagerSnapShot
import me.andannn.aozora.core.pagesource.RoughPageSource
import me.andannn.aozora.ui.common.widgets.rememberRefreshablePagerState
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberBookViewerPresenter(
    card: AozoraBookCard,
    screenSize: Size,
    initialProgress: Long,
    settingRepository: UserDataRepository = getKoin().get(),
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
    private val settingRepository: UserDataRepository,
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

        LaunchedEffect(
            snapshotState?.snapshotVersion,
        ) {
            Napier.d(tag = TAG) { "invoked ${snapshotState?.snapshotVersion}" }
            snapshotFlow { pagerState.settledPage }
                .drop(1)
                .collect { newIndex ->
                    Napier.d(tag = TAG) { "new settled page collected $newIndex" }
                    snapshotState?.pageList[newIndex]
                }
        }

        val scope = rememberCoroutineScope()
        val bookSource: BookPageSource =
            remember {
                RoughPageSource(
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
            pages = snapshotState?.pageList ?: emptyList<AozoraPage>().toImmutableList(),
            pagerState = pagerState,
        ) { eventSink ->
            when (eventSink) {
                else -> {}
            }
        }
    }
}

data class BookViewerState(
    val pageMetadata: PageMetaData,
    val pages: ImmutableList<AozoraPage>,
    val theme: ReaderTheme = ReaderTheme.DYNAMIC,
    val pagerState: PagerState,
    val evenSink: (BookViewerUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface BookViewerUiEvent
