package me.andannn.aosora.core.parser.internal.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement

/**
 * ｜：ルビの付く文字列の始まりを特定する記号
 * （例）一番｜獰悪《どうあく》
 */
object SpecificRubyParser : AozoraPainTextParser {
    private val regex = Regex("""｜(.*?)《(.*?)》""")

    override fun matchAll(input: String): Sequence<TokenMatchResult> =
        regex.findAll(input).map { it.toTokenResult(this) }

    override fun create(match: TokenMatchResult): AozoraElement {
        val base = match.groups[1].value
        val ruby = match.groups[2].value
        return AozoraElement.Ruby(base, ruby)
    }
}
