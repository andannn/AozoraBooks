package me.andannn.aosora.core.parser

interface AozoraElementParser {
    fun matchAll(input: String): Sequence<TokenMatchResult>
    fun create(match: TokenMatchResult): AozoraElement
}