/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher
import me.andannn.aozora.core.pagesource.parser.html.MatchResult

internal object PlainTextMatcher : ElementMatcher {
    override fun match(node: Node): MatchResult {
        if (node !is TextNode) return MatchResult.NotMatched
        if (node.text().isBlank()) return MatchResult.NotMatched

        return MatchResult.ElementMatched(
            AozoraElement.Text(node.text()),
        )
    }
}
