package me.andannn.aosora.ui.reader

import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import me.andannn.aosora.core.common.model.AozoraBookCard
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.common.model.ReaderTheme
import me.andannn.aosora.core.pagesource.AozoraBookPageSource
import me.andannn.aosora.core.pagesource.BookPageSource
import me.andannn.aosora.core.pagesource.PagerSnapShot

@Composable
fun rememberReaderPresenter(
    initialProgress: Long,
    pageMetadata: PageMetaData
) = remember(initialProgress, pageMetadata) {
    ReaderPresenter(
        initialProgress,
        pageMetadata,
    )
}

private const val TAG = "ReaderPresenter"

class ReaderPresenter(
    private val initialProgress: Long,
    private val pageMetadata: PageMetaData,
) : Presenter<ReaderState> {

    @Composable
    override fun present(): ReaderState {
        var snapshotState by remember {
            mutableStateOf<PagerSnapShot<AozoraPage>?>(null)
        }

        val pagerState =
            rememberRefreshablePagerState(
                initialPage = snapshotState?.initialIndex ?: 0,
                version = snapshotState?.snapshotVersion
            ) {
                snapshotState?.pageList?.size ?: 0
            }

        var settledPageState by remember {
            mutableStateOf<AozoraPage?>(null)
        }

        val settledPage by rememberUpdatedState(pagerState.settledPage)

        LaunchedEffect(Unit) {
            var lastKnownVersion: Int = 0

            combine(
                snapshotFlow { settledPage },
                snapshotFlow { snapshotState }.filterNotNull(),
            ) { settledPageIndex, snapShot -> settledPageIndex to snapShot }
                .collect { (settledPageIndex, snapShot) ->
                    val snapShotVersion = snapShot.snapshotVersion
                    if (snapShotVersion == 0) {
                        settledPageState = snapShot.pageList.getOrNull(settledPageIndex)
                        return@collect
                    }

                    val changedByPageList = (lastKnownVersion != snapShotVersion)
                    lastKnownVersion = snapShotVersion

                    if (changedByPageList) {
                        Napier.d(tag = TAG) { "SettledPage changed by pageList." }
                    } else {
                        Napier.d(tag = TAG) { "SettledPage changed by user gesture." }
                        settledPageState = snapShot.pageList.getOrNull(settledPageIndex)
                    }
                }
        }

        val settledPageFlow = remember {
            snapshotFlow { settledPageState }.distinctUntilChanged()
        }

        LaunchedEffect(Unit) {
            settledPageFlow.collect {
                Napier.d(tag = TAG) { "presenter settled page ${it.hashCode()}" }
            }
        }

        Napier.d(tag = TAG) { "pager state: snapshotState version: ${snapshotState?.snapshotVersion} ${snapshotState?.pageList?.map { it.hashCode() }}" }
        Napier.d(tag = TAG) { "pager state: settledPage ${pagerState.settledPage}" }

        val scope = rememberCoroutineScope()
        val bookSource: BookPageSource<AozoraPage> = remember {
            AozoraBookPageSource<AozoraPage.AozoraRoughPage>(
                AozoraBookCard(
                    id = "1",
                    zipUrl = "https://www.aozora.gr.jp/cards/002238/files/61411_ruby_78315.zip",
                    htmlUrl = "https://www.aozora.gr.jp/cards/001095/files/45844_60119.html",
                ),
                scope = scope,
            )
            //            createBookSource(
//                AozoraBookCard(
//                    id = "1",
//                    zipUrl = "https://www.aozora.gr.jp/cards/002238/files/61411_ruby_78315.zip",
//                    htmlUrl = "https://www.aozora.gr.jp/cards/002238/files/61411_78314.html",
//                )
//            )
//            DummySource.createSimpleDummyBookPageSource()
//            DummySource.createDummyLazyBookPageSource(
//                scope = this,
//                settledPageFlow = settledPageFlow,
//                meta = meta
//            )
//            DummySource.createDummySequenceCachedSource(scope = scope)
//            DummySource.createDummyBufferedBookPageSource(
//                meta = meta,
//                scope = this,
//                progress = 3876,
//                settledPageFlow = settledPageFlow
//            )
        }
        LaunchedEffect(
            initialProgress,
            pageMetadata
        ) {
            bookSource
                .getPagerSnapShotFlow(pageMetadata, initialProgress = initialProgress)
                .distinctUntilChanged()
                .collect {
                    Log.d(
                        TAG,
                        "present: New snapshot emit. version ${it.snapshotVersion} currentIndex ${it.initialIndex}, size ${it.pageList.map { it.hashCode() }}"
                    )
                    snapshotState = it
                }
        }

        return ReaderState(
            pages = snapshotState?.pageList ?: emptyList(),
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
    val pages: List<AozoraPage>,
    val theme: ReaderTheme = ReaderTheme.DYNAMIC,
    val pagerState: PagerState,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent {
}
