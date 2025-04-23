package me.andannn.aosora.core.parser.internal.parsers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.AozoraElementParser
import me.andannn.aosora.core.parser.TokenMatchResult
import me.andannn.aosora.core.parser.toTokenResult

object LineBreakParser : AozoraElementParser {

    private val regex = Regex("""\n""")

    override fun matchAll(input: String) = regex.findAll(input).map { it.toTokenResult(this) }

    override fun create(match: TokenMatchResult): AozoraElement {
        return AozoraElement.LineBreak
    }
}