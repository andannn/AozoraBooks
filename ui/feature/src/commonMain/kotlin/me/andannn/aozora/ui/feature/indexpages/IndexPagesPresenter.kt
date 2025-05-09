package me.andannn.aozora.ui.feature.indexpages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.data.AozoraContentsRepository
import me.andannn.aozora.core.data.common.BookColumnItem
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.bookcard.BookCardState
import me.andannn.aozora.ui.feature.screens.BookCardScreen
import me.andannn.core.util.romajiToKana
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberIndexPagesPresenter(
    kana: String,
    aozoraRepository: AozoraContentsRepository = getKoin().get(),
    navigator: Navigator = LocalNavigator.current,
) = remember(
    kana,
    aozoraRepository,
    navigator,
) {
    IndexPagesPresenter(kana, aozoraRepository, navigator)
}

private const val TAG = "IndexPagesPresenter"

class IndexPagesPresenter(
    private val kana: String,
    private val aozoraRepository: AozoraContentsRepository,
    private val navigator: Navigator,
) : Presenter<IndexPagesState> {
    @Composable
    override fun present(): IndexPagesState {
        val kanaLabel =
            remember {
                kana.romajiToKana(isKatakana = true)
            }
        val scope = rememberCoroutineScope()
        val pagingDataFlow =
            rememberRetained {
                aozoraRepository.getBookListPagingFlow(kana).cachedIn(scope)
            }
        val pagingData = pagingDataFlow.collectAsLazyPagingItems()
        return IndexPagesState(
            kanaLabel,
            pagingData,
        ) { event ->
            when (event) {
                IndexPagesUiEvent.OnBack -> {
                    navigator.pop()
                }

                is IndexPagesUiEvent.OnBookClick -> {
                    val url = event.book.title.link
                    Napier.d(tag = TAG) { "goto url $url" }
                    val id = url.substringAfterLast("/card").removeSuffix(".html")
                    val groupId = url.substringAfterLast("/cards/").substringBefore("/")
                    navigator.goTo(
                        BookCardScreen(
                            bookCardId = id,
                            groupId = groupId
                        )
                    )
                }
            }
        }
    }
}

@Stable
data class IndexPagesState(
    val kanaLabel: String,
    val pagingData: LazyPagingItems<BookColumnItem>,
    val evenSink: (IndexPagesUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface IndexPagesUiEvent {
    data object OnBack : IndexPagesUiEvent

    data class OnBookClick(val book: BookColumnItem): IndexPagesUiEvent
}
