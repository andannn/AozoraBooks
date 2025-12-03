/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher
import me.andannn.aozora.core.pagesource.parser.html.MatchResult

internal object ImageMatcher : ElementMatcher {
    override fun match(node: Node): MatchResult {
        if (node !is Element) return MatchResult.NotMatched
        if (node.tagName() != "img") {
            return MatchResult.NotMatched
        }
        val width = node.attr("width").toIntOrNull() ?: return MatchResult.NotMatched
        val height = node.attr("height").toIntOrNull() ?: return MatchResult.NotMatched
        val src = node.attr("src") ?: return MatchResult.NotMatched
        return MatchResult.ElementMatched(
            AozoraElement.Illustration(src, width, height),
        )
    }
}
