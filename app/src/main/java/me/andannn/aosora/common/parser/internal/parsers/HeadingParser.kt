package me.andannn.aosora.common.parser.internal.parsers

import me.andannn.aosora.common.parser.AozoraElement
import me.andannn.aosora.common.parser.AozoraElementParser
import me.andannn.aosora.common.parser.AozoraParser
import me.andannn.aosora.common.parser.AozoraTextStyle
import me.andannn.aosora.common.parser.TokenMatchResult
import me.andannn.aosora.common.parser.toTokenResult


object HeadingParser : AozoraElementParser {
    private val headingContentParser: List<AozoraElementParser> = listOf(
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

        val elements = AozoraParser.parseLine(content, headingContentParser)

        return AozoraElement.Heading(
            style = style,
            indent = indentSize,
            elements = elements,
        )
    }
}