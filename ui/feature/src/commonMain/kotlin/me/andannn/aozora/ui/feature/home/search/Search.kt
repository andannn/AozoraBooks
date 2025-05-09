package me.andannn.aozora.ui.feature.home.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Search(
    modifier: Modifier = Modifier,
    state: SearchState,
) {
    SearchContent(
        modifier = modifier,
        onEvent = state.evenSink,
    )
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    onEvent: (SearchUiEvent) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp),
        columns = GridCells.Fixed(10),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = "作家別",
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            HorizontalDivider()
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = "作品別",
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        items(
            items = KanaItemList,
        ) { item ->
            if (item != null) {
                SearchKanaItem(
                    kana = item.kanaLabel,
                    onClick = {
                        onEvent.invoke(SearchUiEvent.OnClickKanaItem(item.kana))
                    },
                )
            } else {
                Spacer(modifier = Modifier)
            }
        }
    }
}

@Composable
private fun SearchKanaItem(
    modifier: Modifier = Modifier,
    kana: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.aspectRatio(1f).padding(2.dp),
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = kana,
            )
        }
    }
}

data class KanaItem(
    val kana: String,
    val kanaLabel: String,
)

private val KanaItemList =
    listOf(
        KanaItem("a", "あ"),
        KanaItem("ka", "か"),
        KanaItem("sa", "さ"),
        KanaItem("ta", "た"),
        KanaItem("na", "な"),
        KanaItem("ha", "は"),
        KanaItem("ma", "ま"),
        KanaItem("ya", "や"),
        KanaItem("ra", "ら"),
        KanaItem("wa", "わ"),
        KanaItem("i", "い"),
        KanaItem("ki", "き"),
        KanaItem("si", "し"),
        KanaItem("ti", "ち"),
        KanaItem("ni", "に"),
        KanaItem("hi", "ひ"),
        KanaItem("mi", "み"),
        null,
        KanaItem("ri", "り"),
        KanaItem("wo", "を"),
        KanaItem("u", "う"),
        KanaItem("ku", "く"),
        KanaItem("su", "す"),
        KanaItem("tu", "つ"),
        KanaItem("nu", "ぬ"),
        KanaItem("fu", "ふ"),
        KanaItem("mu", "む"),
        KanaItem("yu", "ゆ"),
        KanaItem("ru", "る"),
        KanaItem("n", "ん"),
        KanaItem("e", "え"),
        KanaItem("ke", "け"),
        KanaItem("te", "て"),
        KanaItem("ne", "ね"),
        KanaItem("he", "へ"),
        KanaItem("me", "め"),
        null,
        KanaItem("re", "れ"),
        null,
        KanaItem("o", "お"),
        KanaItem("ko", "こ"),
        KanaItem("se", "せ"),
        KanaItem("so", "そ"),
        KanaItem("to", "と"),
        KanaItem("no", "の"),
        KanaItem("ho", "ほ"),
        KanaItem("mo", "も"),
        KanaItem("yo", "よ"),
        KanaItem("ro", "ろ"),
        KanaItem("zz", "他"),
    )
