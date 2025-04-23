package me.andannn.aosora.core.parser.internal.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement

object PageBreakParser: AozoraPainTextParser {
    private val regex = Regex("""［＃改ページ］""")

    override fun matchAll(input: String): Sequence<TokenMatchResult>  {
        return regex.findAll(input).map { it.toTokenResult(this) }
    }
    override fun create(match: TokenMatchResult): AozoraElement {
        return AozoraElement.PageBreak
    }
}