package me.andannn.aozora.core.pagesource.page.builder

import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.measure.BlockMeasurer
import me.andannn.aozora.core.pagesource.util.divideByTextIndex
import kotlin.math.floor

class RoughPageBuilder(
    private val meta: PageMetaData,
    private val measurer: BlockMeasurer,
) : PageBuilder<AozoraRoughPage> {
    private val renderWidth: Float = meta.renderWidth

    private var currentWidth: Float = 0f
    private val addedBlockList = mutableListOf<AozoraBlock>()

    override fun tryAddBlock(block: AozoraBlock): FillResult {
        val measuredResult = measurer.measure(block)
        if (currentWidth + measuredResult.totalLineHeight <= renderWidth) {
            addedBlockList += block
            currentWidth += measuredResult.totalLineHeight
            return FillResult.FillContinue
        } else {
            val availableLineCount =
                floor((renderWidth - currentWidth) / measuredResult.lineHeightPerLine).toInt()
            if (availableLineCount == 0) {
                return FillResult.Filled(remainBlock = block)
            }
            if (measuredResult.fontStyle != null) {
                val textCountPerLine =
                    floor(measuredResult.availableRenderHeight / measuredResult.fontStyle.baseSize).toInt()
                val (left, right) = block.divideByTextIndex(textCountPerLine * availableLineCount)
                addedBlockList += left
                return FillResult.Filled(remainBlock = right)
            } else {
                // Image block which can not be filled in this page.
                return FillResult.Filled(remainBlock = block)
            }
        }
    }

    override fun build(): AozoraRoughPage =
        AozoraRoughPage(
            metaData = meta,
            blocks = addedBlockList.toImmutableList(),
        )
}
