package me.andannn.aozora.core.data.common

import kotlinx.collections.immutable.ImmutableList

/**
 * Page of reader.
 */
sealed class AozoraPage(
    open val metaData: PageMetaData,
) {
    data class AozoraLayoutPage(
        override val metaData: PageMetaData,
        val lines: ImmutableList<Line>
    ): AozoraPage(metaData)

    data class AozoraRoughPage(
        override  val metaData: PageMetaData,
        val blocks: ImmutableList<AozoraBlock>
    ): AozoraPage(metaData) {
        val progressRange  by lazy {
            blocks.first().byteRange.first..blocks.last().byteRange.last
        }
    }
}


val AozoraPage.AozoraLayoutPage.fullText: String
    get() = lines.fold("") { acc, line ->
        acc + line.fullText
    }
val AozoraPage.AozoraRoughPage.fullText: String
    get() = blocks.fold("") { acc, block ->
        acc + block.fullText
    }

