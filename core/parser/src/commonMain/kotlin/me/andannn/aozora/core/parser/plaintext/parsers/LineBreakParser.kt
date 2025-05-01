package me.andannn.aozora.core.parser.plaintext.parsers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.plaintext.AozoraPainTextParser

object LineBreakParser : AozoraPainTextParser {
    private val regex = Regex("""\n""")

    override fun matchAll(input: String) = regex.findAll(input).map { it.toTokenResult(this) }

    override fun create(match: TokenMatchResult): AozoraElement {
        return AozoraElement.LineBreak
    }
}
