/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource

import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.domain.layouthelper.AozoraPageLayoutHelper
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.domain.model.Page
import me.andannn.aozora.core.domain.model.PageMetaData
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.page.LayoutPageBuilder

class AozoraPageLayoutHelperImpl : AozoraPageLayoutHelper {
    override fun Page.layout(): Page.LayoutPage =
        when (this) {
            is Page.CoverPage -> {
                createCoverPage(
                    pageMetaData,
                    title = title,
                    author = author,
                    subtitle = subtitle,
                ).layout()
            }

            is Page.BibliographicalPage -> {
                error("can not be layout")
            }

            is Page.LayoutPage -> {
                this
            }
        }

    private fun createCoverPage(
        pageMetaData: PageMetaData,
        title: String,
        subtitle: String?,
        author: String,
    ): Page.LayoutPage {
        val builder = LayoutPageBuilder(pageMetaData)
        val coverBlockList = coverBlocks(title, subtitle, author)
        for (block in coverBlockList) {
            builder.tryAddBlock(block)
        }
        return builder.build()
    }

    private fun coverBlocks(
        title: String,
        subtitle: String?,
        author: String,
    ): List<AozoraBlock> =
        listOfNotNull<AozoraBlock>(
            AozoraBlock.TextBlock(
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
                AozoraBlock.TextBlock(
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
            AozoraBlock.TextBlock(
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
        ).toImmutableList()
}
