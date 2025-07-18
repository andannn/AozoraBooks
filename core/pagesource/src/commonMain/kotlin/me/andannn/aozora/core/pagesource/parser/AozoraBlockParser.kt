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
    fun parseLineAsBlock(line: RawLine): AozoraBlock
}

internal class DefaultAozoraBlockParser(
    val parser: AozoraLineParser,
) : AozoraBlockParser {
    var blockIndex: Int = 0

    override fun parseLineAsBlock(line: RawLine): AozoraBlock {
        val matchResultMatched = parser.parseLine(line)
        val elements = matchResultMatched.elements()
        if (matchResultMatched.size == 1 && elements.count { it is AozoraElement.Illustration } == 1) {
            return AozoraBlock.Image(
                blockIndex = blockIndex++,
                elements = elements,
            )
        } else if (matchResultMatched.size == 2 && matchResultMatched[0] is MatchResult.BlockMatched) {
            val block = matchResultMatched[0] as MatchResult.BlockMatched
            return AozoraBlock.TextBlock(
                blockIndex = blockIndex++,
                elements = elements,
                indent = block.indent,
                textStyle = block.style,
                maxCharacterPerLine = block.maxLength,
            )
        } else {
            return AozoraBlock.TextBlock(
                blockIndex = blockIndex++,
                textStyle = AozoraTextStyle.PARAGRAPH,
                elements = elements,
            )
        }
    }
}
