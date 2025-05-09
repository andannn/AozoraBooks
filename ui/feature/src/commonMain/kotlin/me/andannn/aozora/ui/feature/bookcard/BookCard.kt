package me.andannn.aozora.ui.feature.bookcard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.andannn.aozora.core.data.common.AozoraBookCard

@Composable
fun BookCard(
    state: BookCardState,
    modifier: Modifier = Modifier,
) {
    BookCardContent(
        modifier = modifier,
        bookCardInfo = state.bookCardInfo,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookCardContent(
    modifier: Modifier = Modifier,
    bookCardInfo: AozoraBookCard?,
) {
    if (bookCardInfo == null) {
        CircularProgressIndicator()
        return
    }

    Scaffold(
        modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(bookCardInfo.title)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                        },
                    ) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
                    }
                },
            )
        },
    ) {
        LazyColumn(Modifier.padding(it)) {
            item {
                Text(bookCardInfo.toString())
            }
        }
    }
}
