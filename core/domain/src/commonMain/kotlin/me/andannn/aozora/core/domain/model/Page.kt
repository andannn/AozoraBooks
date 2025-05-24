/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

/**
 * Page of reader.
 */
sealed class AozoraPage(
    open val pageMetaData: PageMetaData,
) {
    data class AozoraCoverPage(
        override val pageMetaData: PageMetaData,
        val title: String,
        val author: String,
        val subtitle: String?,
    ) : AozoraPage(pageMetaData)

    data class AozoraRoughPage(
        override val pageMetaData: PageMetaData,
        val blocks: ImmutableList<Block>,
    ) : AozoraPage(pageMetaData) {
        val pageProgress by lazy {
            blocks.first().blockIndex..blocks.last().blockIndex
        }
    }

    data class AozoraBibliographicalPage(
        override val pageMetaData: PageMetaData,
        val html: String,
    ) : AozoraPage(pageMetaData)
}

data class LayoutPage(
    val pageMetaData: PageMetaData,
    val lines: ImmutableList<Line>,
) {
    val contentWidth by lazy {
        lines.fold(0.dp) { acc, line ->
            acc + line.lineHeight
        }
    }
}
