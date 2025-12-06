/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.domain.model.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.domain.model.PageMetaData
import me.andannn.aozora.core.pagesource.measure.BlockMeasureScope
import me.andannn.aozora.core.pagesource.util.divideByTextIndex
import kotlin.math.floor

internal class RoughPageBuilder constructor(
    private val meta: PageMetaData,
    private val measurer: BlockMeasureScope,
) : PageBuilder<AozoraRoughPage> {
    private val renderWidth: Dp = meta.renderWidth

    private var currentWidth: Dp = 0.dp
    private val addedBlockList = mutableListOf<AozoraBlock>()

    override fun tryAddBlock(block: AozoraBlock): FillResult {
        val measuredResult = measurer.measure(block)
        if (currentWidth + measuredResult.totalLineHeightDp <= renderWidth) {
            // Page filled.
            addedBlockList += block
            currentWidth += measuredResult.totalLineHeightDp
            return FillResult.FillContinue
        } else {
            val availableLineCount =
                floor((renderWidth - currentWidth) / measuredResult.lineHeightDpPerLine).toInt()
            if (availableLineCount == 0) {
                // No space to fill this block.
                return FillResult.Filled(remainBlock = block)
            }

            // Page is not filled, but we can fill some part of this block.
            if (measuredResult.fontStyle != null) {
                val textCountPerLine = measuredResult.availableTextCountPerLine
                val (left, right) = block.divideByTextIndex(textCountPerLine * availableLineCount)
                addedBlockList += left

                // return FillResult with the remaining block
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
