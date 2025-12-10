/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.io.files.Path

/**
 * Page of reader.
 */
sealed interface Page {
    data class CoverPage(
        val title: String,
        val author: String,
        val subtitle: String?,
    ) : Page

    sealed interface ContentPage : Page {
        val pageProgress: IntRange
        val contentWidth: Dp
    }

    data class ImagePage constructor(
        val element: AozoraElement.Illustration,
        override val contentWidth: Dp,
        private val elementIndex: Int,
        private val imageDictionary: Path,
    ) : ContentPage {
        override val pageProgress: IntRange = elementIndex..elementIndex

        fun getImageFile(): String = Path(imageDictionary, element.filename).toString()
    }

    data class TextLayoutPage(
        val lines: ImmutableList<LineWithBlockIndex>,
    ) : ContentPage {
        data class LineWithBlockIndex(
            val line: Line,
            val blockIndex: Int,
        )

        override val contentWidth by lazy {
            lines.fold(0.dp) { acc, line ->
                acc + line.line.lineHeight
            }
        }

        override val pageProgress by lazy {
            lines.first().blockIndex..lines.last().blockIndex
        }
    }

    data class BibliographicalPage(
        val html: String,
    ) : Page
}
