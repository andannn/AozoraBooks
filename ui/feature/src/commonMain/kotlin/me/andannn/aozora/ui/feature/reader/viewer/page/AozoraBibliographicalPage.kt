package me.andannn.aozora.ui.feature.reader.viewer.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.data.common.AozoraPage.AozoraBibliographicalPage
import me.andannn.core.util.parseBookSourceAsAnnotatedString

@Composable
fun AozoraBibliographicalPage(
    modifier: Modifier = Modifier,
    page: AozoraBibliographicalPage,
) {
    Column(modifier = modifier.fillMaxSize().padding(horizontal = 48.dp)) {
        val content =
            remember(page.html) {
                parseBookSourceAsAnnotatedString(page.html)
            }

        Spacer(modifier = Modifier.weight(1f))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.body2,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.weight(1f))
    }
}
