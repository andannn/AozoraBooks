package me.andannn.aozora.ui.feature.home.library

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Library(
    state: LibraryState,
    modifier: Modifier = Modifier,
) {
    LibraryContent(
        modifier = modifier,
        onEvent = state.evenSink
    )
}

@Composable
fun LibraryContent(modifier: Modifier, onEvent: (LibraryUiEvent) -> Unit) {
    Column(
        modifier = modifier
    ) {
        Card(
            onClick = {
                onEvent.invoke(LibraryUiEvent.OnCardClick("789"))
            },
        ) {
            Text(text = "吾輩は猫である")
        }

        Card(
            onClick = {
                onEvent.invoke(LibraryUiEvent.OnCardClick("301"))
            },
        ) {
            Text(text = "人間失格")
        }

        Card(
            onClick = {
                onEvent.invoke(LibraryUiEvent.OnCardClick("56648"))
            },
        ) {
            Text(text = "人間椅子")
        }
        Card(
            onClick = {
                onEvent.invoke(LibraryUiEvent.OnCardClick("60756"))
            },
        ) {
            Text(text = "現代語訳　平家物語")
        }
    }

}
