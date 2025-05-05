package me.andannn.aozora.ui.feature.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.core.data.common.AozoraBookCard

@Composable
fun rememberReaderPresenter(cardId: String) =
    remember(
        cardId,
    ) {
        ReaderPresenter(cardId)
    }

class ReaderPresenter(
    private val cardId: String,
) : Presenter<ReaderState> {
    @Composable
    override fun present(): ReaderState {
        val scope = rememberCoroutineScope()
        return ReaderState(getCardById(cardId)) { event ->
            when (event) {
                else -> {}
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

@Stable
data class ReaderState(
    val bookCard: AozoraBookCard,
    val evenSink: (ReaderUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderUiEvent
