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
import kotlinx.io.files.Path
import me.andannn.aosora.core.common.model.FontSizeLevel
import me.andannn.aosora.core.common.model.FontType
import me.andannn.aosora.core.common.model.LineSpacing
import me.andannn.aosora.core.pager.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.common.model.ReaderTheme
import me.andannn.aosora.core.common.model.TopMargin
import me.andannn.aosora.core.source.createBookSource

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
            createBookSource(Path(context.filesDir.toString()))
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
                        originalHeight = renderSize.height,
                        originalWidth = renderSize.width,
                        additionalTopMargin = TopMargin.MEDIUM,
                        fontSizeLevel = FontSizeLevel.Level_4,
                        fontType = FontType.NOTO_SERIF,
                        lineSpacing = LineSpacing.MEDIUM
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
    val theme: ReaderTheme = ReaderTheme.DYNAMIC,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent {
}
