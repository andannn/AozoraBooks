package me.andannn.aozora.core.data.common

import kotlinx.collections.immutable.ImmutableList

/**
 * Page of reader.
 */
sealed class AozoraPage(
    open val pageMetaData: PageMetaData,
) {
    abstract val pageProgress: IntRange

    data class AozoraCoverPage(
        override val pageMetaData: PageMetaData,
        val title: String,
        val author: String,
        val subtitle: String?,
    ) : AozoraPage(pageMetaData) {
        override val pageProgress: IntRange = Int.MIN_VALUE..Int.MIN_VALUE
    }

    data class AozoraRoughPage(
        override val pageMetaData: PageMetaData,
        val blocks: ImmutableList<Block>,
    ) : AozoraPage(pageMetaData) {
        override val pageProgress by lazy {
            blocks.first().blockIndex..blocks.last().blockIndex
        }
    }

    data class AozoraBibliographicalPage(
        override val pageMetaData: PageMetaData,
        val html: String,
    ) : AozoraPage(pageMetaData) {
        override val pageProgress: IntRange = Int.MAX_VALUE..Int.MAX_VALUE
    }
}

data class LayoutPage(
    val pageMetaData: PageMetaData,
    val lines: ImmutableList<Line>,
) {
    val contentWidth by lazy {
        lines.fold(0f) { acc, line ->
            acc + line.lineHeight
        }
    }
}
