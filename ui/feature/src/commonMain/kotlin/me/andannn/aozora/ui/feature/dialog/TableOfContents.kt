package me.andannn.aozora.ui.feature.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.core.data.common.BookInfo
import me.andannn.aozora.core.data.common.TableOfContent
import me.andannn.aozora.core.pagesource.BookPageSource
import me.andannn.aozora.core.pagesource.LocalBookPageSource
import me.andannn.aozora.ui.common.dialog.DialogAction
import me.andannn.aozora.ui.common.dialog.DialogId
import me.andannn.aozora.ui.common.dialog.DialogType

object TableOfContentsDialogId : DialogId {
    override val dialogType: DialogType = DialogType.ModalBottomSheet

    @Composable
    override fun Content(onAction: (DialogAction) -> Unit) {
        TableOfContentsDialog(onAction)
    }
}

data class OnJumpTo(
    val lineNumber: Int,
) : DialogAction

@Composable
fun rememberTableOfContentsDialogPresenter(bookSource: BookPageSource = LocalBookPageSource.current) =
    remember(bookSource) {
        TableOfContentsPresenter(bookSource)
    }

class TableOfContentsPresenter(
    private val bookSource: BookPageSource,
) : Presenter<TableOfContentsState> {
    @Composable
    override fun present(): TableOfContentsState {
        val bookInfo by produceState<BookInfo?>(null) {
            value = bookSource.getBookInfo()
        }
        return TableOfContentsState(
            tableOfContentsList = bookInfo?.tableOfContentList ?: emptyList(),
        )
    }
}

data class TableOfContentsState(
    val tableOfContentsList: List<TableOfContent> = emptyList(),
) : CircuitUiState

@Composable
fun TableOfContentsDialog(
    onAction: (DialogAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val presenter = rememberTableOfContentsDialogPresenter()
    val state = presenter.present()

    TableOfContentsDialogContent(
        modifier = modifier,
        state = state,
        onClickTableOfContent = {
            onAction(OnJumpTo(it.lineNumber))
        },
    )
}

@Composable
fun TableOfContentsDialogContent(
    state: TableOfContentsState,
    modifier: Modifier = Modifier,
    onClickTableOfContent: (TableOfContent) -> Unit = {},
) {
    Column(modifier = modifier) {
        Text("Table of Contents", style = MaterialTheme.typography.titleMedium)
        val maxHeadingLevel = state.tableOfContentsList.minOfOrNull { it.headingLevel }
        Spacer(modifier = Modifier.height(24.dp))

        state.tableOfContentsList.forEach { tableOfContent ->
            TableOfContentItem(
                modifier = modifier.fillMaxWidth(),
                spacerCount = maxHeadingLevel?.let { tableOfContent.headingLevel - it },
                tableOfContent = tableOfContent,
                onClick = {
                    onClickTableOfContent(tableOfContent)
                },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun TableOfContentItem(
    modifier: Modifier = Modifier,
    tableOfContent: TableOfContent,
    onClick: () -> Unit = {},
    spacerCount: Int?,
) {
    Row(modifier.clickable(onClick = onClick).padding(vertical = 12.dp, horizontal = 16.dp)) {
        if (spacerCount != null) {
            repeat(spacerCount) {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
        Text(tableOfContent.title, style = getHeadingLevel(tableOfContent.headingLevel))
    }
}

@Composable
fun getHeadingLevel(headingLevel: Int): TextStyle =
    when (headingLevel) {
        1 -> MaterialTheme.typography.headlineLarge
        2 -> MaterialTheme.typography.headlineMedium
        3 -> MaterialTheme.typography.headlineSmall
        4 -> MaterialTheme.typography.bodyLarge
        5 -> MaterialTheme.typography.bodyMedium
        6 -> MaterialTheme.typography.bodySmall
        else -> MaterialTheme.typography.bodySmall
    }
