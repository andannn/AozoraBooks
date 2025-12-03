/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.ui.common.theme.AozoraTheme

@Preview
@Composable
private fun BookColumnItemViewPreview() {
    AozoraTheme {
        BookColumnItemView(
            item =
                AozoraBookCard(
                    id = "",
                    title = "Title",
                    titleKana = "",
                    authorId = "",
                    author = "author",
                    authorUrl = null,
                    zipUrl = null,
                    subTitle = "subTitle",
                    htmlUrl = null,
                    source = null,
                    characterType = null,
                    staffData = null,
                    haveCopyRight = false,
                    cardUrl = "",
                    authorDataList = emptyList(),
                ),
            index = 1,
        )
    }
}
