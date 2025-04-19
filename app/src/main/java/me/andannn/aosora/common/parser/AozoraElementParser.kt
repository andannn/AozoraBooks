package me.andannn.aosora.common.parser

interface AozoraElementParser {
    fun matchAll(input: String): Sequence<TokenMatchResult>
    fun create(match: TokenMatchResult): AozoraElement
}