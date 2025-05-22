/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.common.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.measure.BlockMeasurer
import me.andannn.aozora.core.pagesource.util.divideByTextIndex
import kotlin.math.floor

internal class RoughPageBuilder(
    private val meta: PageMetaData,
    private val measurer: BlockMeasurer,
) : PageBuilder<AozoraRoughPage> {
    private val renderWidth: Dp = meta.renderWidth

    private var currentWidth: Dp = 0.dp
    private val addedBlockList = mutableListOf<AozoraBlock>()

    override fun tryAddBlock(block: AozoraBlock): FillResult {
        val measuredResult = measurer.measure(block)
        if (currentWidth + measuredResult.totalLineHeightDp <= renderWidth) {
            addedBlockList += block
            currentWidth += measuredResult.totalLineHeightDp
            return FillResult.FillContinue
        } else {
            val availableLineCount =
                floor((renderWidth - currentWidth) / measuredResult.lineHeightDpPerLine).toInt()
            if (availableLineCount == 0) {
                return FillResult.Filled(remainBlock = block)
            }
            if (measuredResult.fontStyle != null) {
                val textCountPerLine =
                    floor(measuredResult.availableRenderHeightDp / measuredResult.fontStyle.baseSizeDp).toInt()
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
            pageMetaData = meta,
            blocks = addedBlockList.toImmutableList(),
        )
}
