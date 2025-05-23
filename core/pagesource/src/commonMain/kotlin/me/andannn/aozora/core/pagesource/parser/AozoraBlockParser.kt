/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock

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
        val elements = parser.parseLine(line)

        if (elements.size == 2 && elements[0] is AozoraElement.Heading) {
            val heading = (elements[0] as AozoraElement.Heading)
            val indent = heading.indent
            return AozoraBlock.TextBlock(
                blockIndex = blockIndex++,
                elements = heading.elements + elements[1],
                indent = indent,
                textStyle = heading.style,
            )
        } else if (elements.size == 2 && elements[0] is AozoraElement.SpecialParagraph) {
            val paragraph = elements[0] as AozoraElement.SpecialParagraph
            return AozoraBlock.TextBlock(
                blockIndex = blockIndex++,
                elements = paragraph.elements + elements[1],
                indent = paragraph.indent,
                textStyle = AozoraTextStyle.PARAGRAPH,
                maxCharacterPerLine = paragraph.maxLength,
            )
        } else if (elements.size == 1 && elements[0] is AozoraElement.Illustration) {
            return AozoraBlock.Image(
                blockIndex = blockIndex++,
                elements = elements,
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
