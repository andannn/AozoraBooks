package me.andannn.aosora.core.parser.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.plaintext.AozoraPainTextParser

object LineBreakParser : AozoraPainTextParser {

    private val regex = Regex("""\n""")

    override fun matchAll(input: String) = regex.findAll(input).map { it.toTokenResult(this) }

    override fun create(match: TokenMatchResult): AozoraElement {
        return AozoraElement.LineBreak
    }
}