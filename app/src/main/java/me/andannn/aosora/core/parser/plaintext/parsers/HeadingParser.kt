package me.andannn.aosora.core.parser.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.AozoraTextStyle
import me.andannn.aosora.core.parser.plaintext.AozoraPainTextParser
import me.andannn.aosora.core.parser.plaintext.PlainTextLineParser

object HeadingParser : AozoraPainTextParser {
    private val headingContentParser: List<AozoraPainTextParser> = listOf(
        SpecificRubyParser,
        RubyParser,
        EmphasisParser,
    )

    private val regex = Regex("［＃([０-９]+)字下げ］?(.+?)［＃「(.+?)」は(.+?)］")

    override fun matchAll(input: String): Sequence<TokenMatchResult> {
        return regex.findAll(input).map { it.toTokenResult(this) }
    }

    override fun create(match: TokenMatchResult): AozoraElement {
        val fullWidthDigits = match.groups[1].value
        val content = match.groups[2].value
        val type = match.groups[4].value
        val indentSize = fullWidthDigits.mapNotNull {
            "０１２３４５６７８９".indexOf(it).takeIf { idx -> idx >= 0 }
        }.joinToString("").toIntOrNull() ?: 0

        val style = when (type) {
            "中見出し" -> AozoraTextStyle.HEADING_MEDIUM
            "大見出し" -> AozoraTextStyle.HEADING_LARGE
            "小見出し" -> AozoraTextStyle.HEADING_SMALL
            else -> AozoraTextStyle.PARAGRAPH
        }

        val elements = PlainTextLineParser.parseLineInternal(content, headingContentParser)

        return AozoraElement.Heading(
            style = style,
            indent = indentSize,
            elements = elements,
        )
    }
}