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
sealed class Page(
    open val pageMetaData: PageMetaData,
) {
    data class CoverPage(
        override val pageMetaData: PageMetaData,
        val title: String,
        val author: String,
        val subtitle: String?,
    ) : Page(pageMetaData)

    data class LayoutPage(
        override val pageMetaData: PageMetaData,
        val lines: ImmutableList<LineWithBlockIndex>,
    ) : Page(pageMetaData) {
        data class LineWithBlockIndex(
            val line: Line,
            val blockIndex: Int,
        )

        val contentWidth by lazy {
            lines.fold(0.dp) { acc, line ->
                acc + line.line.lineHeight
            }
        }

        val pageProgress by lazy {
            lines.first().blockIndex..lines.last().blockIndex
        }
    }

    data class BibliographicalPage(
        override val pageMetaData: PageMetaData,
        val html: String,
    ) : Page(pageMetaData)
}
