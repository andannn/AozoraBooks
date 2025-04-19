package me.andannn.aosora.common.parser.internal.parsers

import me.andannn.aosora.common.parser.AozoraElement
import me.andannn.aosora.common.parser.AozoraElementParser
import me.andannn.aosora.common.parser.TokenMatchResult
import me.andannn.aosora.common.parser.toTokenResult

/**
 * ｜：ルビの付く文字列の始まりを特定する記号
 * （例）一番｜獰悪《どうあく》
 */
object SpecificRubyParser : AozoraElementParser {
    private val regex = Regex("""｜(.*?)《(.*?)》""")

    override fun matchAll(input: String): Sequence<TokenMatchResult> =
        regex.findAll(input).map { it.toTokenResult(this) }

    override fun create(match: TokenMatchResult): AozoraElement {
        val base = match.groups[1].value
        val ruby = match.groups[2].value
        return AozoraElement.Ruby(base, ruby)
    }
}
