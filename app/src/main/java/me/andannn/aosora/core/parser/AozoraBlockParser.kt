package me.andannn.aosora.core.parser

import me.andannn.aosora.core.common.model.AozoraBlock
import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.common.model.BlockType
import me.andannn.aosora.core.common.util.RawLine
import me.andannn.aosora.core.parser.html.HtmlLineParser

/**
 * Create block parser.
 */
fun createBlockParser(lineParser: AozoraLineParser = HtmlLineParser): AozoraBlockParser =
    DefaultAozoraBlockParser(lineParser)

interface AozoraBlockParser {
    /**
     * parse line to block
     */
    fun parseLineAsBlock(line: RawLine): AozoraBlock
}

private class DefaultAozoraBlockParser(
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
