/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import me.andannn.aozora.core.domain.model.Page
import me.andannn.aozora.core.domain.model.PageMetaData

@Composable
fun ImagePageView(
    modifier: Modifier = Modifier,
    pageMetaData: PageMetaData,
    page: Page.ImagePage,
) {
    Box(
        modifier =
            modifier
                .padding(
                    horizontal = pageMetaData.offset.first,
                    vertical = pageMetaData.offset.second,
                ).size(
                    width = pageMetaData.renderWidth,
                    height = pageMetaData.renderHeight,
                ),
    ) {
        AsyncImage(
            model = page.getImageFile(),
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
    }
}
