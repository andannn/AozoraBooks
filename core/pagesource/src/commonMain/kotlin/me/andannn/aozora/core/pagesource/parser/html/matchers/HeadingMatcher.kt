/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraElement.Heading
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher
import me.andannn.aozora.core.pagesource.parser.html.parseAsAozoraElements

internal object HeadingMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        if (node !is Element) return null
        if (node.tagName() != "div") return null

        val className = node.attr("class")
        val indent = className.split("_").getOrNull(1)?.toIntOrNull() ?: return null
        val child = node.children().getOrNull(0) ?: return null
        val headingLevel = child.tagName().removePrefix("h").toIntOrNull() ?: return null
        val styleName = child.attr("class")
        val style =
            when {
                styleName.contains("naka-midashi") -> AozoraTextStyle.HEADING_MEDIUM
                styleName.contains("o-midashi") -> AozoraTextStyle.HEADING_LARGE
                else -> AozoraTextStyle.PARAGRAPH
            }
        val contentNodes = child.selectFirst(".midashi_anchor")?.childNodes() ?: return null
        val elements = contentNodes.parseAsAozoraElements()

        return Heading(
            indent = indent,
            style = style,
            headingLevel = headingLevel,
            elements = elements,
        )
    }
}
