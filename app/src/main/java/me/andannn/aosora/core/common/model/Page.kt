package me.andannn.aosora.core.common.model

import kotlinx.collections.immutable.ImmutableList
import me.andannn.aosora.core.common.model.AozoraPage.AozoraLayoutPage
import me.andannn.aosora.core.common.model.AozoraPage.AozoraRoughPage

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


val AozoraLayoutPage.fullText: String
    get() = lines.fold("") { acc, line ->
        acc + line.fullText
    }
val AozoraRoughPage.fullText: String
    get() = blocks.fold("") { acc, block ->
        acc + block.fullText
    }

