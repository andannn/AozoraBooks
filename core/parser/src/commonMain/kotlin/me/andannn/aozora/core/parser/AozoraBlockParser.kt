package me.andannn.aozora.core.parser

import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.BlockType


interface AozoraBlockParser {
    /**
     * parse line to block
     */
    fun parseLineAsBlock(line: RawLine): AozoraBlock
}

internal class DefaultAozoraBlockParser(
    val parser: AozoraLineParser
) : AozoraBlockParser {

    override fun parseLineAsBlock(
        line: RawLine,
    ): AozoraBlock {
        val elements = parser.parseLine(line)
        var blockType: BlockType?
        val blockElements: List<AozoraElement>
        var indent = 0

        if (elements.size == 1 && elements[0] is AozoraElement.Heading) {
            val heading = (elements[0] as AozoraElement.Heading)
            blockElements = heading.elements
            blockType = BlockType.Heading(indent = heading.indent, style = heading.style)
            indent = heading.indent
        } else if (elements.size == 1 && elements[0] is AozoraElement.Illustration) {
            blockType = BlockType.Image
            blockElements = elements
        } else {
            blockType = BlockType.Text()
            blockElements = elements
        }
        return AozoraBlock(
            elements = blockElements,
            blockType = blockType,
            byteRange = line.index..(line.index + line.length)
        )
    }
}
