package me.andannn.aosora.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import me.andannn.aosora.core.common.model.FontSizeLevel
import me.andannn.aosora.core.common.model.FontType
import me.andannn.aosora.core.common.model.LineSpacing
import me.andannn.aosora.core.common.model.PageContext
import me.andannn.aosora.core.common.model.TopMargin
import me.andannn.aosora.core.common.model.next
import me.andannn.aosora.ui.reader.Reader
import me.andannn.aosora.ui.reader.rememberReaderPresenter

@Composable
fun Home(state: HomeState, modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) {
        HomeContent(
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    val localDensity = LocalDensity.current
    val initial = remember {
        mutableStateOf(0L)
    }
    val fontLevel = remember {
        mutableStateOf(FontSizeLevel.Level_4)
    }
    BoxWithConstraints(modifier = modifier) {
        val maxHeight = with(localDensity) { this@BoxWithConstraints.maxHeight.toPx() }
        val maxWidth = with(localDensity) { maxWidth.toPx() }
        val presenter = rememberReaderPresenter(
            initialProgress = initial.value,
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
            Reader(
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