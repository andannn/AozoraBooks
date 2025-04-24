package me.andannn.aosora.core.parser.plaintext

import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.parser.plaintext.parsers.TokenMatchResult

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