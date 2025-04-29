package me.andannn.aozora.core.parser.plaintext.parsers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.plaintext.AozoraPainTextParser

/**
 * 《》：ルビ
 * （例）吾輩《わがはい》
 */
object RubyParser : AozoraPainTextParser {
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
