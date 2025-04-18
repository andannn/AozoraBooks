package me.andannn.aosora.common.util.parser

import me.andannn.aosora.common.model.AozoraString

interface AozoraTokenParser {
    fun match(input: String, startIndex: Int): MatchResult?
    fun create(match: MatchResult): AozoraString
}