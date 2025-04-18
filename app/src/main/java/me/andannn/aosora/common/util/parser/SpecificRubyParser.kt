package me.andannn.aosora.common.util.parser

import me.andannn.aosora.common.model.AozoraString

/**
 * ｜：ルビの付く文字列の始まりを特定する記号
 * （例）一番｜獰悪《どうあく》
 */
object SpecificRubyParser : AozoraTokenParser {
    private val regex = Regex("""｜(.*?)《(.*?)》""")

    override fun match(input: String, startIndex: Int) =
        regex.find(input, startIndex)

    override fun create(match: MatchResult): AozoraString {
        val base = match.groupValues[1]
        val ruby = match.groupValues[2]
        return AozoraString.Ruby(base, ruby)
    }
}
