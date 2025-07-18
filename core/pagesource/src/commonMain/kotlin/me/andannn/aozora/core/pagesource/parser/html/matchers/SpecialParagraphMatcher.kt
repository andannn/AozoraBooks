/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.pagesource.parser.elements
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher
import me.andannn.aozora.core.pagesource.parser.html.MatchResult
import me.andannn.aozora.core.pagesource.parser.html.matchAsMatchResults

internal object SpecialParagraphMatcher : ElementMatcher {
    override fun match(node: Node): MatchResult {
        if (node !is Element) return MatchResult.NotMatched
        if (node.tagName() != "div") return MatchResult.NotMatched
        val (divResult, contentNodes) = parseDivElementRecursive(node)
        if (divResult.isEmpty()) return MatchResult.NotMatched
        if (contentNodes.isEmpty()) return MatchResult.NotMatched
        val elements = contentNodes.matchAsMatchResults()

        return MatchResult.BlockMatched(
            indent = (divResult.firstOrNull { it is Div.Indent } as? Div.Indent?)?.indent ?: 0,
            maxLength = (divResult.firstOrNull { it is Div.Max } as? Div.Max?)?.value,
            elements = elements.elements(),
        )
    }

    private fun parseDivElementRecursive(element: Element): Pair<List<Div>, List<Node>> {
        if (!element.isDiv()) return listOf<Div>() to emptyList<Node>()

        var current = element
        val results = mutableListOf<Div>()
        while (current.isDiv()) {
            val result = parseDivElement(current) ?: break
            results.add(result)
            val firstNode =
                current
                    .children()
                    .firstOrNull()
                    .takeIf { it?.isDiv() == true } ?: break
            current = firstNode
        }
        return results to current.childNodes()
    }

    private fun Element.isDiv(): Boolean = tagName() == "div"

    private fun parseDivElement(element: Element): Div? {
        if (element.tagName() != "div") return null
        val style = element.attr("style")
        if (style.startsWith("width:")) {
            return Div.Max(
                value =
                    style.substringAfter("width: ").substringBefore("em").toIntOrNull()
                        ?: return null,
            )
        } else if (style.startsWith("margin-left:")) {
            return Div.Indent(
                indent =
                    style.substringAfter("margin-left: ").substringBefore("em").toIntOrNull()
                        ?: return null,
            )
        }
        return null
    }
}

private sealed interface Div {
    data class Indent(
        val indent: Int,
    ) : Div

    data class Max(
        val value: Int,
    ) : Div
}
