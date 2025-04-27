package me.andannn.aosora.core.parser.plaintext

import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.common.util.RawLine
import me.andannn.aosora.core.parser.AozoraLineParser
import me.andannn.aosora.core.parser.plaintext.parsers.EmphasisParser
import me.andannn.aosora.core.parser.plaintext.parsers.HeadingParser
import me.andannn.aosora.core.parser.plaintext.parsers.IllustrationNotionParser
import me.andannn.aosora.core.parser.plaintext.parsers.IndentParser
import me.andannn.aosora.core.parser.plaintext.parsers.LineBreakParser
import me.andannn.aosora.core.parser.plaintext.parsers.PageBreakParser
import me.andannn.aosora.core.parser.plaintext.parsers.RubyParser
import me.andannn.aosora.core.parser.plaintext.parsers.SpecificRubyParser
import me.andannn.aosora.core.parser.plaintext.parsers.TokenMatchResult
import kotlin.collections.plusAssign

object PlainTextLineParser: AozoraLineParser {
    override fun parseLine(line: RawLine): List<AozoraElement> {
        return parseLineInternal(line.content)
    }

    /**
     * parse line to elements
     */
    fun parseLineInternal(
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
    LineBreakParser,
)

private data class PrioritizedMatch(
    val match: TokenMatchResult,
    val priority: Int
)
