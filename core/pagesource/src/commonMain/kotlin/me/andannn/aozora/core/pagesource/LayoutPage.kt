/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource

import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.data.common.LayoutPage
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.measure.DefaultMeasurer
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.page.LayoutPageBuilder

fun AozoraPage.layout(): LayoutPage {
    return when (this) {
        is AozoraRoughPage -> {
            val builder =
                LayoutPageBuilder(pageMetaData, DefaultMeasurer(pageMetaData), forceAddBlock = true)
            this.blocks.filterIsInstance<AozoraBlock>().forEach {
                builder.tryAddBlock(it)
            }
            return builder.build()
        }

        is AozoraPage.AozoraCoverPage ->
            createCoverPage(
                pageMetaData,
                title = title,
                author = author,
                subtitle = subtitle,
            ).layout()

        is AozoraPage.AozoraBibliographicalPage -> error("can not be layout")
    }
}

private fun createCoverPage(
    pageMetaData: PageMetaData,
    title: String,
    subtitle: String?,
    author: String,
): AozoraRoughPage =
    AozoraRoughPage(
        pageMetaData = pageMetaData,
        blocks =
            listOfNotNull<AozoraBlock>(
                AozoraBlock.Heading(
                    blockIndex = -1,
                    textStyle = AozoraTextStyle.HEADING_LARGE,
                    indent = 2,
                    elements =
                        listOfNotNull(
                            AozoraElement.Text(
                                text = title,
                            ),
                            AozoraElement.LineBreak,
                        ),
                ),
                subtitle?.let {
                    AozoraBlock.Heading(
                        blockIndex = -1,
                        textStyle = AozoraTextStyle.HEADING_LARGE,
                        indent = 2,
                        elements =
                            listOfNotNull(
                                AozoraElement.Text(
                                    text = it,
                                ),
                                AozoraElement.LineBreak,
                            ),
                    )
                },
                AozoraBlock.Heading(
                    blockIndex = -1,
                    textStyle = AozoraTextStyle.HEADING_MEDIUM,
                    indent = 3,
                    elements =
                        listOfNotNull(
                            AozoraElement.Text(
                                text = author,
                            ),
                            AozoraElement.LineBreak,
                        ),
                ),
            ).toImmutableList(),
    )
