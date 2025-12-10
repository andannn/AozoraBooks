package me.andannn.aozora.ui.feature.reader.viewer.page

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.andannn.aozora.core.domain.model.Page
import me.andannn.aozora.core.domain.model.PageMetaData

@Composable
fun ImagePageView(
    modifier: Modifier = Modifier,
    pageMetaData: PageMetaData,
    page: Page.ImagePage,
) {
    Box(modifier = modifier) {
        Text("TODO : Image $page")
    }
}
