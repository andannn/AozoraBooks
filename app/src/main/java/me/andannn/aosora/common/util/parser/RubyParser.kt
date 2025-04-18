package me.andannn.aosora.common.util.parser

import me.andannn.aosora.common.model.AozoraString

/**
 * 《》：ルビ
 * （例）吾輩《わがはい》
 */
object RubyParser : AozoraTokenParser {
    private val regex = Regex("""([一-龯々〆ヵヶ]+)《(.*?)》""")

    override fun match(input: String, startIndex: Int): MatchResult? {
        return regex.find(input, startIndex)
    }

    override fun create(match: MatchResult): AozoraString {
        val base = match.groupValues[1]
        val ruby = match.groupValues[2]
        return AozoraString.Ruby(base, ruby)
    }
}
