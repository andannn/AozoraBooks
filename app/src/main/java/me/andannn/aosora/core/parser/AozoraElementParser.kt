package me.andannn.aosora.core.parser

interface AozoraElementParser {
    /**
     * match all tokens in [input]
     */
    fun matchAll(input: String): Sequence<TokenMatchResult>

    /**
     * create [AozoraElement] from [match]
     */
    fun create(match: TokenMatchResult): AozoraElement
}