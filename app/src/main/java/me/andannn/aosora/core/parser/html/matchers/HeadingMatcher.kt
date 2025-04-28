package me.andannn.aosora.core.parser.html.matchers

import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.common.model.AozoraElement.Heading
import me.andannn.aosora.core.common.model.AozoraTextStyle
import me.andannn.aosora.core.parser.html.ElementMatcher
import me.andannn.aosora.core.parser.html.parseAsAozoraElements
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

object HeadingMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        node as? Element ?: return null
        if (node.tagName() != "div") return null

        // <h4 class="naka-midashi"><a class="midashi_anchor" id="midashi30">二</a></h4>
        val className = node.attr("class")
        val indent = className.split("_").getOrNull(1)?.toIntOrNull() ?: return null
        val child = node.children().getOrNull(0) ?: return null
        val styleName = child.attr("class") ?: return null
        val style = when (styleName) {
            "naka-midashi" -> AozoraTextStyle.HEADING_MEDIUM

            else -> AozoraTextStyle.PARAGRAPH
        }
        val contentNodes = child.selectFirst(".midashi_anchor")?.childNodes() ?: return null
        val elements = contentNodes.parseAsAozoraElements()
        return Heading(
            indent = indent,
            style = style,
            elements = elements
        )
    }
}