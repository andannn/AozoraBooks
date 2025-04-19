package me.andannn.aosora.common.parser.internal.parsers

import me.andannn.aosora.common.parser.AozoraElement
import me.andannn.aosora.common.parser.AozoraElementParser
import me.andannn.aosora.common.parser.TokenMatchResult
import me.andannn.aosora.common.parser.toTokenResult

/**
 * 《》：ルビ
 * （例）吾輩《わがはい》
 */
object RubyParser : AozoraElementParser {
    private val regex = Regex("""([一-龯々〆ヵヶ]+)《(.*?)》""")

    override fun matchAll(input: String): Sequence<TokenMatchResult> {
        return regex.findAll(input).map { it.toTokenResult(this) }
    }

    override fun create(match: TokenMatchResult): AozoraElement {
        val base = match.groups[1].value
        val ruby = match.groups[2].value
        return AozoraElement.Ruby(base, ruby)
    }
}
