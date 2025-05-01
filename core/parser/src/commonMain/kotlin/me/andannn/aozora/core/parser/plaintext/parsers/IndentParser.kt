package me.andannn.aozora.core.parser.plaintext.parsers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.plaintext.AozoraPainTextParser

object IndentParser : AozoraPainTextParser {
    private val regex = Regex("""［＃([０-９]+)字下げ］""")

    override fun matchAll(input: String): Sequence<TokenMatchResult> = regex.findAll(input).map { it.toTokenResult(this) }

    override fun create(match: TokenMatchResult): AozoraElement {
        val fullWidthDigits = match.groups[1].value

        // 全角数字 → 半角数字
        val indentSize =
            fullWidthDigits.mapNotNull {
                "０１２３４５６７８９".indexOf(it).takeIf { idx -> idx >= 0 }
            }.joinToString("").toIntOrNull() ?: 0

        return AozoraElement.Indent(count = indentSize)
    }
}
