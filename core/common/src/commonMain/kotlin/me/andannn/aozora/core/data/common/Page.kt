package me.andannn.aozora.core.data.common

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

    data class AozoraLayoutPage(
        override val pageMetaData: PageMetaData,
        val lines: ImmutableList<Line>,
    ) : AozoraPage(pageMetaData) {
        val contentWidth by lazy {
            lines.fold(0f) { acc, line ->
                acc + line.lineHeight
            }
        }
    }

    data class AozoraRoughPage(
        override val pageMetaData: PageMetaData,
        val blocks: ImmutableList<AozoraBlock>,
    ) : AozoraPage(pageMetaData) {
        val progressRange by lazy {
            blocks.first().byteRange.first..blocks.last().byteRange.last
        }
    }
}

val AozoraPage.AozoraLayoutPage.fullText: String
    get() =
        lines.fold("") { acc, line ->
            acc + line.fullText
        }
val AozoraPage.AozoraRoughPage.fullText: String
    get() =
        blocks.fold("") { acc, block ->
            acc + block.fullText
        }
