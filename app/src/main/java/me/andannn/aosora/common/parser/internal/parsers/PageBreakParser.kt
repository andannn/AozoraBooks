package me.andannn.aosora.common.parser.internal.parsers

import me.andannn.aosora.common.parser.AozoraElement
import me.andannn.aosora.common.parser.AozoraElementParser
import me.andannn.aosora.common.parser.TokenMatchResult
import me.andannn.aosora.common.parser.toTokenResult

object PageBreakParser: AozoraElementParser {
    private val regex = Regex("""［＃改ページ］""")

    override fun matchAll(input: String): Sequence<TokenMatchResult>  {
        return regex.findAll(input).map { it.toTokenResult(this) }
    }
    override fun create(match: TokenMatchResult): AozoraElement {
        return AozoraElement.PageBreak
    }
}