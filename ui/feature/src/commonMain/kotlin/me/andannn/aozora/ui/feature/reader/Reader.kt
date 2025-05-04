package me.andannn.aozora.ui.feature.reader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.ui.feature.reader.viewer.BookViewer
import me.andannn.aozora.ui.feature.reader.viewer.rememberBookViewerPresenter

@Composable
fun Reader(
    state: ReaderState,
    modifier: Modifier = Modifier,
) {
    ReaderContent(
        cardId = state.cardId,
        modifier = modifier,
        onEvent = state.evenSink,
    )
}

@Composable
private fun ReaderContent(
    cardId: String,
    modifier: Modifier = Modifier,
    onEvent: (ReaderUiEvent) -> Unit,
) {
    val localDensity = LocalDensity.current
    val initial =
        remember {
            mutableLongStateOf(0L)
        }
    BoxWithConstraints(modifier = modifier) {
        val maxHeight = with(localDensity) { this@BoxWithConstraints.maxHeight.toPx() }
        val maxWidth = with(localDensity) { maxWidth.toPx() }
        val presenter =
            rememberBookViewerPresenter(
                card = getCardById(cardId),
                initialProgress = initial.longValue,
                screenSize = Size(maxWidth, maxHeight),
            )
        val state = presenter.present()
        BookViewer(
            state = state,
        )

        Column {
            Box(modifier = Modifier.height(30.dp))

            TextButton(onClick = {
                onEvent.invoke(ReaderUiEvent.OnOpenFontSetting)
            }) {
                Text("Open Font Setting")
            }
        }
    }
}

private fun getCardById(id: String) =
    when (id) {
        "301" -> {
            AozoraBookCard(
                id = "301",
                zipUrl = "https://www.aozora.gr.jp/cards/000035/files/301_ruby_5915.zip",
                htmlUrl = "https://www.aozora.gr.jp/cards/000035/files/301_14912.html",
            )
        }

        "789" -> {
            AozoraBookCard(
                id = "789",
                zipUrl = "https://www.aozora.gr.jp/cards/000148/files/789_ruby_5639.zip",
                htmlUrl = "https://www.aozora.gr.jp/cards/000148/files/789_14547.html",
            )
        }

        else -> error("")
    }
