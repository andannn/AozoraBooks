/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.pagesource.parser.html.MatchResult

/**
 * parse line to block
 */
internal interface AozoraLineParser {
    fun parseLine(line: RawLine): List<MatchResult.Matched>
}

internal fun List<MatchResult.Matched>.elements(): List<AozoraElement> =
    this.fold(emptyList<AozoraElement>()) { acc, matchResult ->
        when (matchResult) {
            is MatchResult.ElementMatched -> acc + matchResult.element
            is MatchResult.BlockMatched -> acc + matchResult.elements
            MatchResult.NotMatched -> acc
        }
    }
