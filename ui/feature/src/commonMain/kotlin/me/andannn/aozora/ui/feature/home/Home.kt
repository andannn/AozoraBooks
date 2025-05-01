package me.andannn.aozora.ui.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Home(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    HomeContent(
        modifier = modifier,
        state = state,
    )
}

@Composable
fun HomeContent(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    Scaffold {
        Column(
            modifier = modifier.padding(it),
        ) {
            Card(
                onClick = {
                    state.evenSink.invoke(HomeUiEvent.OnCardClick("789"))
                },
            ) {
                Text(text = "吾輩は猫である")
            }

            Card(
                onClick = {
                    state.evenSink.invoke(HomeUiEvent.OnCardClick("301"))
                },
            ) {
                Text(text = "人間失格")
            }
        }
    }
}
