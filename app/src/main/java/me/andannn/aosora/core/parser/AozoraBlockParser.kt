package me.andannn.aosora.core.parser

import me.andannn.aosora.core.parser.html.HtmlLineParser

fun createBlockParser(parser: AozoraLineParser = HtmlLineParser) =
    AozoraBlockParser(parser)

class AozoraBlockParser(
    val parser: AozoraLineParser
) {
    /**
     * parse line to block
     */
    fun parseLineAsBlock(
        line: String,
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
            elements = blockElements + AozoraElement.LineBreak,
            blockType = blockType,
        )
    }
}
