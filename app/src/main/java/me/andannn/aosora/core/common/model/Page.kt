package me.andannn.aosora.core.common.model

import kotlinx.collections.immutable.ImmutableList

/**
 * Page of reader.
 */
data class AozoraPage(
    val metaData: PaperLayout,
    val lines: ImmutableList<Line>
)

val AozoraPage.fullText: String
    get() = lines.fold("") { acc, line ->
        acc + line.fullText
    }

