package me.andannn.aosora.common.parser

import me.andannn.aosora.common.parser.internal.parsers.EmphasisParser
import me.andannn.aosora.common.parser.internal.parsers.HeadingParser
import me.andannn.aosora.common.parser.internal.parsers.IllustrationNotionParser
import me.andannn.aosora.common.parser.internal.parsers.IndentParser
import me.andannn.aosora.common.parser.internal.parsers.PageBreakParser
import me.andannn.aosora.common.parser.internal.parsers.RubyParser
import me.andannn.aosora.common.parser.internal.parsers.SpecificRubyParser

sealed interface BlockType {
    data object Text: BlockType
    data object Heading: BlockType
    data object Image: BlockType
}

object AozoraParser {
    private val LocalParsers = listOf(
        SpecificRubyParser,
        RubyParser,
        IllustrationNotionParser,
        EmphasisParser,
        IndentParser,
        PageBreakParser,
        HeadingParser
    )

    fun parseLineAsBlock(
        line: String,
        parsers: List<AozoraElementParser> = LocalParsers
    ): AozoraBlock {
        val elements = parseLine(line, parsers)
        var style: BlockStyle?
        var blockType: BlockType?
        if (elements.size == 2 && elements[1] is AozoraElement.Heading) {
            val heading = (elements[1] as AozoraElement.Heading)
            style = parseStyle(heading.style, heading.indent)
            blockType = BlockType.Heading
        } else if (elements.size == 1 && elements[0] is AozoraElement.Illustration) {
            blockType = BlockType.Image
            style = parseStyle(AozoraTextStyle.PARAGRAPH, 0)
        } else {
            blockType = BlockType.Text
            style = parseStyle(AozoraTextStyle.PARAGRAPH, 0)
        }
        return AozoraBlock(
            elements = elements,
            style = style,
            blockType = blockType
        )
    }

    fun parseLine(
        line: String,
        parsers: List<AozoraElementParser> = LocalParsers
    ): List<AozoraElement> {
        val result = mutableListOf<AozoraElement>()
        val matches = collectMatches(line, parsers)
        var matchIndex = 0
        var index = 0

        while (index < line.length) {
            // 快速推进 matchIndex 到有效位置
            while (matchIndex < matches.size && matches[matchIndex].range.last < index) {
                matchIndex++
            }

            val nextMatch = matches.getOrNull(matchIndex)
            if (nextMatch == null || nextMatch.range.first >= line.length) {
                result += AozoraElement.Text(line.substring(index))
                break
            }

            if (nextMatch.range.first > index) {
                result += AozoraElement.Text(line.substring(index, nextMatch.range.first))
            }

            result += nextMatch.parser.create(nextMatch)
            index = nextMatch.range.last + 1
            matchIndex++
        }

        return result
    }

    private fun collectMatches(
        line: String,
        parsers: List<AozoraElementParser>
    ): List<TokenMatchResult> {
        val results = mutableListOf<TokenMatchResult>()
        for (parser in parsers) {
            parser.matchAll(line).forEach { match ->
                results += match
            }
        }
        return results.sortedBy { it.range.first }
    }

    private fun parseStyle(style: AozoraTextStyle, blockIndent: Int = 0): BlockStyle {
        return when (style) {
            AozoraTextStyle.HEADING_LARGE -> BlockStyle(
                fontSize = 24,
                lineHeight = 32,
                blockIndent = blockIndent,
            )

            AozoraTextStyle.HEADING_MEDIUM -> BlockStyle(
                fontSize = 20,
                lineHeight = 28,
                blockIndent = blockIndent,
            )

            AozoraTextStyle.HEADING_SMALL -> BlockStyle(
                fontSize = 18,
                lineHeight = 26,
                blockIndent = blockIndent,
            )

            AozoraTextStyle.PARAGRAPH -> BlockStyle(
                fontSize = 16,
                lineHeight = 24,
                blockIndent = blockIndent,
            )
        }
    }
}
