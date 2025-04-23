package me.andannn.aosora.core.parser.internal.plaintext

import me.andannn.aosora.core.parser.AozoraBlock
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.BlockType
import me.andannn.aosora.core.parser.internal.plaintext.parsers.AozoraPainTextParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.EmphasisParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.HeadingParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.IllustrationNotionParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.IndentParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.LineBreakParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.PageBreakParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.RubyParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.SpecificRubyParser
import me.andannn.aosora.core.parser.internal.plaintext.parsers.TokenMatchResult
import kotlin.collections.plus
import kotlin.collections.plusAssign

object AozoraPlainTextParser {

    /**
     * parse line to block
     */
    fun parseLineAsBlock(
        line: String,
        parsers: List<AozoraPainTextParser> = Parsers
    ): AozoraBlock {
        val elements = parseLine(line, parsers)

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

    /**
     * parse line to elements
     */
    fun parseLine(
        line: String,
        parsers: List<AozoraPainTextParser> = Parsers
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
        parsers: List<AozoraPainTextParser>
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
}

private val Parsers = listOf(
    HeadingParser,
    SpecificRubyParser,
    RubyParser,
    LineBreakParser,
    IllustrationNotionParser,
    EmphasisParser,
    IndentParser,
    PageBreakParser,
)

private data class PrioritizedMatch(
    val match: TokenMatchResult,
    val priority: Int
)
