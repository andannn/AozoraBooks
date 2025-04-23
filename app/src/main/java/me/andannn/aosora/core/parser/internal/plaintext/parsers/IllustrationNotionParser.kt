package me.andannn.aosora.core.parser.internal.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement

/**
 * 例：
 * ［＃挿絵１（fig51160_01.png、横478×縦438）入る］
 */
object IllustrationNotionParser: AozoraPainTextParser {
    private val regex = Regex("""［＃.+（([^)、]+)、横(\d+)×縦(\d+)）入る］""")

    override fun matchAll(input: String): Sequence<TokenMatchResult> {
        return regex.findAll(input).map { it.toTokenResult(this) }
    }

    override fun create(match: TokenMatchResult): AozoraElement {
        val filename = match.groups[1].value
        val width = match.groups[2].value.toIntOrNull()
        val height = match.groups[3].value.toIntOrNull()

        return AozoraElement.Illustration(
            filename = filename,
            width = width,
            height = height
        )
    }
}