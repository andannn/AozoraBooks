package me.andannn.aosora.core.parser.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.EmphasisStyle
import me.andannn.aosora.core.parser.plaintext.AozoraPainTextParser
import me.andannn.aosora.core.parser.plaintext.mapTokenMatchResultWitCutStart

object EmphasisParser : AozoraPainTextParser {
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
