package me.andannn.aozora.ui.common.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.andannn.aozora.ui.common.theme.AozoraTheme
import me.andannn.aozora.ui.common.theme.NotoSerifJpFontFamily


@Preview
@Composable
private fun PreviewBookCardPreview() {
    AozoraTheme {
        Surface(modifier = Modifier.padding(12.dp)) {
            PreviewBookCard(
                Modifier.padding(12.dp),
                title = "吾輩は猫である",
                author = "夏目漱石",
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBookCardPreviewLongDark() {
    AozoraTheme(darkTheme = true) {
        PreviewBookCard(
            title = "吾輩は猫である 吾輩は猫である 吾輩は猫である 吾輩は猫である",
            author = "夏目漱石 夏目漱石 夏目漱石 夏目漱石 夏目漱石",
        )
    }
}
