package me.andannn.aosora.ui.reader

import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Size
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import me.andannn.aosora.core.common.model.FontSizeLevel
import me.andannn.aosora.core.common.model.FontType
import me.andannn.aosora.core.common.model.LineSpacing
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.common.model.ReaderTheme
import me.andannn.aosora.core.common.model.TopMargin
import me.andannn.aosora.core.pagesource.createDummyLazyBookPageSource

@Composable
fun rememberReaderPresenter(
    renderSize: Size
) = remember(renderSize) {
    ReaderPresenter(
        renderSize = renderSize
    )
}

private const val TAG = "ReaderPresenter"

class ReaderPresenter(
    private val renderSize: Size
) : Presenter<ReaderState> {

    @Composable
    override fun present(): ReaderState {
        val scope = rememberCoroutineScope()
        val pages = rememberRetained {
            mutableStateOf<List<AozoraPage>>(emptyList())
        }

        // trigger for refresh page. update this value only when need to reset initial page index.
        val initialPageIndex = remember {
            mutableIntStateOf(0)
        }

        val pagerState = rememberRefreshablePagerState(
            initialPageIndex.intValue
        ) {
            pages.value.size
        }

        val settledPageIndex = rememberUpdatedState(pagerState.settledPage)

        val meta = PageMetaData(
            originalHeight = renderSize.height,
            originalWidth = renderSize.width,
            additionalTopMargin = TopMargin.MEDIUM,
            fontSizeLevel = FontSizeLevel.Level_4,
            fontType = FontType.NOTO_SERIF,
            lineSpacing = LineSpacing.MEDIUM
        )

        val settledPageFlow = remember {
            combine(
                snapshotFlow { settledPageIndex.value },
                snapshotFlow { pages.value }
            ) { settledPageIndex, pages ->
                pages.getOrNull(settledPageIndex)
            }
        }

        LaunchedEffect(Unit) {
            settledPageFlow.collect {
                Napier.d(tag = TAG) { "settled page $it" }
            }
        }

        Napier.d(tag = TAG) { "pager state: initialPageIndex $initialPageIndex" }
        Napier.d(tag = TAG) { "pager state: settledPage ${pagerState.settledPage}" }
        Napier.d(tag = TAG) { "pager state: currentPage ${pagerState.currentPage}" }
        Napier.d(tag = TAG) { "pager state: targetPage ${pagerState.targetPage}" }
        LaunchedEffect(
            Unit
        ) {
            pages.value = emptyList<AozoraPage>()

//            createBookSource(
//                AozoraBookCard(
//                    id = "1",
//                    zipUrl = "https://www.aozora.gr.jp/cards/002238/files/61411_ruby_78315.zip",
//                    htmlUrl = "https://www.aozora.gr.jp/cards/002238/files/61411_78314.html",
//                )
//            )
//            createSimpleDummyBookPageSource(
//                meta
//            )
            createDummyLazyBookPageSource(
                scope = scope,
                settledPageFlow = settledPageFlow,
                meta = meta
            )
                .pagerSnapShotFlow
                .distinctUntilChanged()
                .collect {
                    Log.d(
                        TAG,
                        "present: New snapshot emit. currentIndex ${it.initialIndex}, size ${it.pageList.size}"
                    )
                    pages.value = it.pageList
                    if (it.initialIndex != null) {
                        initialPageIndex.intValue = it.initialIndex
                    }
                }
        }

        return ReaderState(
            pages = pages.value.toImmutableList(),
            pagerState = pagerState
        ) { eventSink ->
            when (eventSink) {
                else -> {}
            }
        }
    }
}

@Stable
data class ReaderState(
    val pages: ImmutableList<AozoraPage>,
    val theme: ReaderTheme = ReaderTheme.DYNAMIC,
    val pagerState: PagerState,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent {
}
