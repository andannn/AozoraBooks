package me.andannn.aosora.core.parser.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.plaintext.AozoraPainTextParser

object PageBreakParser: AozoraPainTextParser {
    private val regex = Regex("""［＃改ページ］""")

    override fun matchAll(input: String): Sequence<TokenMatchResult>  {
        return regex.findAll(input).map { it.toTokenResult(this) }
    }
    override fun create(match: TokenMatchResult): AozoraElement {
        return AozoraElement.PageBreak
    }
}