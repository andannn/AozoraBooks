package me.andannn.aosora.core.parser.internal.plaintext.parsers

import me.andannn.aosora.core.parser.AozoraElement

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