package me.andannn.aosora.core.parser

import androidx.annotation.VisibleForTesting
import me.andannn.aosora.core.parser.internal.parsers.EmphasisParser
import me.andannn.aosora.core.parser.internal.parsers.HeadingParser
import me.andannn.aosora.core.parser.internal.parsers.IllustrationNotionParser
import me.andannn.aosora.core.parser.internal.parsers.IndentParser
import me.andannn.aosora.core.parser.internal.parsers.PageBreakParser
import me.andannn.aosora.core.parser.internal.parsers.RubyParser
import me.andannn.aosora.core.parser.internal.parsers.SpecificRubyParser

sealed interface BlockType {
    data object Text : BlockType
    data object Heading : BlockType
    data object Image : BlockType
}

private val Parsers = listOf(
    HeadingParser,
    SpecificRubyParser,
    RubyParser,
    IllustrationNotionParser,
    EmphasisParser,
    IndentParser,
    PageBreakParser,
)

data class PrioritizedMatch(
    val match: TokenMatchResult,
    val priority: Int
)

object AozoraParser {

    fun parseLineAsBlock(
        line: String,
        parsers: List<AozoraElementParser> = Parsers
    ): AozoraBlock {
        val elements = parseLine(line, parsers)

        var style: BlockStyle?
        var blockType: BlockType?
        val blockElements: List<AozoraElement>
        var indent = 0

        if (elements.size == 1 && elements[0] is AozoraElement.Heading) {
            val heading = (elements[0] as AozoraElement.Heading)
            blockElements = heading.elements
            style = parseStyle(heading.style, heading.indent)
            blockType = BlockType.Heading
            indent = heading.indent
        } else if (elements.size == 1 && elements[0] is AozoraElement.Illustration) {
            blockType = BlockType.Image
            style = parseStyle(AozoraTextStyle.PARAGRAPH, 0)
            blockElements = elements
        } else {
            blockType = BlockType.Text
            style = parseStyle(AozoraTextStyle.PARAGRAPH, 0)
            blockElements = elements
        }
        return AozoraBlock(
            elements = blockElements + AozoraElement.LineBreak,
            style = style,
            blockType = blockType,
        )
    }

    @VisibleForTesting
    fun parseLine(
        line: String,
        parsers: List<AozoraElementParser> = Parsers
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
        val allMatches = parsers
            .flatMapIndexed { index, parser ->
                val priority = index
                parser.matchAll(line).map { PrioritizedMatch(it, priority) }
            }
            .sortedWith(compareBy({ it.match.range.first }, { it.priority }))

        val result = mutableListOf<TokenMatchResult>()
        var lastRangeEnd = -1

        for (prioritized in allMatches) {
            if (prioritized.match.range.first > lastRangeEnd) {
                result += prioritized.match
                lastRangeEnd = prioritized.match.range.last
            }
        }

        return result
    }

    private fun parseStyle(style: AozoraTextStyle, blockIndent: Int = 0): BlockStyle {
        return when (style) {
            AozoraTextStyle.HEADING_LARGE -> BlockStyle(
                fontSize = 24,
                lineHeightMultiplier = 1.333f,
                blockIndent = blockIndent,
            )

            AozoraTextStyle.HEADING_MEDIUM -> BlockStyle(
                fontSize = 20,
                lineHeightMultiplier = 1.4f,
                blockIndent = blockIndent,
            )

            AozoraTextStyle.HEADING_SMALL -> BlockStyle(
                fontSize = 18,
                lineHeightMultiplier = 1.444f,
                blockIndent = blockIndent,
            )

            AozoraTextStyle.PARAGRAPH -> BlockStyle(
                fontSize = 16,
                lineHeightMultiplier = 1.5f,
                blockIndent = blockIndent,
            )
        }
    }
}
