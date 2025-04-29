package me.andannn.aozora.ui.feature.reader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.PageContext
import me.andannn.aozora.core.data.common.TopMargin
import me.andannn.aozora.core.data.common.next
import me.andannn.aozora.ui.feature.reader.viewer.BookViewer
import me.andannn.aozora.ui.feature.reader.viewer.rememberBookViewerPresenter

@Composable
fun Reader(state: ReaderState, modifier: Modifier = Modifier) {
    ReaderContent(
        cardId = state.cardId,
        modifier = modifier
    )
}

@Composable
private fun ReaderContent(cardId: String, modifier: Modifier = Modifier) {
    val localDensity = LocalDensity.current
    val initial = remember {
        mutableLongStateOf(0L)
    }
    val fontLevel = remember {
        mutableStateOf(FontSizeLevel.Level_4)
    }
    BoxWithConstraints(modifier = modifier) {
        val maxHeight = with(localDensity) { this@BoxWithConstraints.maxHeight.toPx() }
        val maxWidth = with(localDensity) { maxWidth.toPx() }
        val presenter = rememberBookViewerPresenter(
            card = getCardById(cardId),
            initialProgress = initial.longValue,
            pageMetadata = PageContext(
                originalHeight = maxHeight,
                originalWidth = maxWidth,
                additionalTopMargin = TopMargin.MEDIUM,
                fontSizeLevel = fontLevel.value,
                fontType = FontType.NOTO_SERIF,
                lineSpacing = LineSpacing.MEDIUM
            ),
        )
        Box {
            BookViewer(
                state = presenter.present()
            )

            Column {
                TextButton(onClick = {
                    initial.value += 1000
                }) {
                    Text("AAA")
                }
                TextButton(onClick = {
                    fontLevel.value = fontLevel.value.next()
                }) {
                    Text("Change FontLevel")
                }
            }
        }
    }
}

private fun getCardById(id: String)  = when (id) {
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