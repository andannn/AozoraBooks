package me.andannn.aozora.core.parser.plaintext

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.plaintext.parsers.TokenMatchResult

interface AozoraPainTextParser {
    /**
     * match all tokens in [input]
     */
    fun matchAll(input: String): Sequence<TokenMatchResult>

    /**
     * create [AozoraElement] from [match]
     */
    fun create(match: TokenMatchResult): AozoraElement
}
