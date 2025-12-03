/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.EmphasisStyle
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher
import me.andannn.aozora.core.pagesource.parser.html.MatchResult

internal object EmphasisMatcher : ElementMatcher {
    override fun match(node: Node): MatchResult {
        if (node !is Element) return MatchResult.NotMatched

        val className = node.attr("class")

        if (className.lowercase() != "sesame_dot") {
            return MatchResult.NotMatched
        }

        val style =
            when (node.tagName()) {
                "em" -> EmphasisStyle.Bouten
                "strong" -> EmphasisStyle.Strong
                else -> error("unknown emphasis style: ${node.tagName()}")
            }
        val sesameDot = node.text()
        return MatchResult.ElementMatched(
            AozoraElement.Emphasis(sesameDot, style),
        )
    }
}
