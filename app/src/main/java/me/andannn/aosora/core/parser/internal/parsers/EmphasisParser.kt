package me.andannn.aosora.core.parser.internal.parsers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.AozoraElementParser
import me.andannn.aosora.core.parser.EmphasisStyle
import me.andannn.aosora.core.parser.TokenMatchResult
import me.andannn.aosora.core.parser.internal.util.mapTokenMatchResultWitCutStart

object EmphasisParser : AozoraElementParser {
    private val regex = Regex("""(.+?)［＃「(.+?)」に傍点］""")

    override fun matchAll(input: String): Sequence<TokenMatchResult> {
        val matchSequence = regex.findAll(input)
        return matchSequence.map { match ->
            match.mapTokenMatchResultWitCutStart(input, this)
        }.filterNotNull()
    }

    override fun create(match: TokenMatchResult): AozoraElement {
        val target = match.groups[2].value

        return AozoraElement.Emphasis(
            text = target,
            emphasisStyle = EmphasisStyle.Bouten
        )
    }
}
