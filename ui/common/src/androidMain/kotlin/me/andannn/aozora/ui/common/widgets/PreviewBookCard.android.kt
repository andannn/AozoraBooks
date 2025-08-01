/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.ReadProgress
import me.andannn.aozora.ui.common.theme.AozoraTheme

@Preview
@Composable
private fun PreviewBookCardPreview() {
    AozoraTheme {
        Surface(modifier = Modifier.padding(12.dp)) {
            PreviewBookCard(
                title = "吾輩は猫である",
                author = "夏目漱石",
                progress = ReadProgress.None,
                userMarkRead = false,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBookCardWithSubTitlePreview() {
    AozoraTheme {
        Surface(modifier = Modifier.padding(12.dp)) {
            PreviewBookCard(
                title = "吾輩は猫である",
                subTitle = "subTitle subTitle subTitle subTitle",
                author = "夏目漱石",
                progress = ReadProgress.None,
                userMarkRead = false,
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
            progress = ReadProgress.Done,
            userMarkRead = false,
        )
    }
}
