package me.andannn.aosora.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
    BoxWithConstraints(modifier = modifier.padding(horizontal = 30.dp, vertical = 20.dp)) {
        val maxHeight = with(localDensity) { this@BoxWithConstraints.maxHeight.toPx() }
        val maxWidth = with(localDensity) { maxWidth.toPx() }
        val presenter = rememberReaderPresenter(
            renderSize = Size(maxWidth, maxHeight)
        )
        Reader(
            state = presenter.present()
        )
    }
}