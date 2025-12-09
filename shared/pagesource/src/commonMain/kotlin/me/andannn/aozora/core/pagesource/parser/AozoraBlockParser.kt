/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.parser.html.MatchResult

internal interface AozoraBlockParser {
    /**
     * parse line to block
     */
    fun parseLineAsBlock(line: RawLine): List<AozoraBlock>
}

internal class DefaultAozoraBlockParser(
    val parser: AozoraLineParser,
) : AozoraBlockParser {
    var blockIndex: Int = 0

    override fun parseLineAsBlock(line: RawLine): List<AozoraBlock> {
        val matchResultMatched = parser.parseLine(line)
        return matchResultMatched
            .map { matched ->
                when (matched) {
                    is MatchResult.BlockMatched -> {
                        AozoraBlock.TextBlock(
                            blockIndex = blockIndex,
                            elements = matched.elements,
                            indent = matched.indent,
                            textStyle = matched.style,
                            maxCharacterPerLine = matched.maxLength,
                        )
                    }

                    is MatchResult.ElementMatched -> {
                        when (matched.element) {
                            is AozoraElement.Illustration -> {
                                AozoraBlock.Image(
                                    blockIndex = blockIndex,
                                    image = matched.element,
                                )
                            }

                            else -> {
                                AozoraBlock.TextBlock(
                                    blockIndex = blockIndex,
                                    textStyle = AozoraTextStyle.PARAGRAPH,
                                    elements = listOf(matched.element),
                                )
                            }
                        }
                    }
                }
            }.also {
                blockIndex++
            }
    }
}
