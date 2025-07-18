/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.pagesource.parser.elements
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher
import me.andannn.aozora.core.pagesource.parser.html.MatchResult
import me.andannn.aozora.core.pagesource.parser.html.matchAsMatchResults

internal object HeadingMatcher : ElementMatcher {
    override fun match(node: Node): MatchResult {
        if (node !is Element) return MatchResult.NotMatched
        if (node.tagName() != "div") return MatchResult.NotMatched

        val className = node.attr("class")
        val indent =
            className.split("_").getOrNull(1)?.toIntOrNull() ?: return MatchResult.NotMatched
        val child = node.children().getOrNull(0) ?: return MatchResult.NotMatched
        val headingLevel =
            child.tagName().removePrefix("h").toIntOrNull() ?: return MatchResult.NotMatched
        val styleName = child.attr("class")
        val style =
            when {
                styleName.contains("naka-midashi") -> AozoraTextStyle.HEADING_MEDIUM
                styleName.contains("o-midashi") -> AozoraTextStyle.HEADING_LARGE
                else -> AozoraTextStyle.PARAGRAPH
            }
        val contentNodes =
            child.selectFirst(".midashi_anchor")?.childNodes() ?: return MatchResult.NotMatched
        val result = contentNodes.matchAsMatchResults()

        return MatchResult.BlockMatched(
            elements = result.elements(),
            indent = indent,
            style = style,
            headingLevel = headingLevel,
        )
    }
}
