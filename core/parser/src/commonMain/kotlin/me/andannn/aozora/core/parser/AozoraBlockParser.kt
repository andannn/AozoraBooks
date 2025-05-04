package me.andannn.aozora.core.parser

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.Block

interface AozoraBlockParser {
    /**
     * parse line to block
     */
    fun parseLineAsBlock(line: RawLine): Block
}

class DefaultAozoraBlockParser(
    val parser: AozoraLineParser,
) : AozoraBlockParser {
    var blockIndex: Int = 0

    override fun parseLineAsBlock(line: RawLine): Block {
        val elements = parser.parseLine(line)

        if (elements.size == 2 && elements[0] is AozoraElement.Heading) {
            val heading = (elements[0] as AozoraElement.Heading)
            val indent = heading.indent
            return Block.Heading(
                blockIndex = blockIndex++,
                elements = heading.elements,
                indent = indent,
                textStyle = heading.style,
            )
        } else if (elements.size == 1 && elements[0] is AozoraElement.Illustration) {
            return Block.Image(
                blockIndex = blockIndex++,
                elements = elements,
            )
        } else {
            return Block.Paragraph(
                blockIndex = blockIndex++,
                elements = elements,
            )
        }
    }
}
