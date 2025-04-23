package me.andannn.aosora.core.parser.internal.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement

object LineBreakParser : AozoraPainTextParser {

    private val regex = Regex("""\n""")

    override fun matchAll(input: String) = regex.findAll(input).map { it.toTokenResult(this) }

    override fun create(match: TokenMatchResult): AozoraElement {
        return AozoraElement.LineBreak
    }
}