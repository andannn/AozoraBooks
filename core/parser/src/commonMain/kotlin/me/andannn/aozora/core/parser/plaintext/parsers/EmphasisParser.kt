package me.andannn.aozora.core.parser.plaintext.parsers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.EmphasisStyle
import me.andannn.aozora.core.parser.plaintext.AozoraPainTextParser
import me.andannn.aozora.core.parser.plaintext.mapTokenMatchResultWitCutStart

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
            emphasisStyle = EmphasisStyle.Bouten,
        )
    }
}
