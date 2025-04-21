package me.andannn.aosora.ui.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import me.andannn.aosora.core.common.FontSizeLevel
import me.andannn.aosora.core.pager.AozoraPage
import me.andannn.aosora.core.measure.PageMetaData
import me.andannn.aosora.core.source.BookSource

@Composable
fun rememberReaderPresenter(
    renderSize: Size
) = remember(renderSize) {
    ReaderPresenter(
        renderSize = renderSize
    )
}

class ReaderPresenter(
    private val renderSize: Size
) : Presenter<ReaderState> {

    @Composable
    override fun present(): ReaderState {
        val context = LocalContext.current
        val source = rememberRetained {
            BookSource(context.filesDir.toPath())
        }

        val pages = rememberRetained {
            mutableStateListOf<AozoraPage>()
        }

        LaunchedEffect(
            Unit
        ) {
            pages.clear()

            source
                .pageSource(
                    PageMetaData(
                        renderHeight = renderSize.height,
                        renderWidth = renderSize.width,
                        fontSizeLevel = FontSizeLevel.Level_9
                    )
                )
                .asFlow()
                .flowOn(Dispatchers.IO)
                .collect {
                    pages.add(it)
                }
        }

        return ReaderState(
            pages = pages.toImmutableList()
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
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent {
}
