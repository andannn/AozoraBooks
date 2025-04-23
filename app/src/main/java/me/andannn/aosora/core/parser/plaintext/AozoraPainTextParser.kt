package me.andannn.aosora.core.parser.plaintext

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.plaintext.parsers.TokenMatchResult

interface AozoraPainTextParser {
    /**
     * match all tokens in [input]
     */
    fun matchAll(input: String): Sequence<TokenMatchResult>

    /**
     * create [me.andannn.aosora.core.parser.AozoraElement] from [match]
     */
    fun create(match: TokenMatchResult): AozoraElement
}