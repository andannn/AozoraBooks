package me.andannn.aozora.core.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraElement.Heading
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.parser.html.ElementMatcher
import me.andannn.aozora.core.parser.html.parseAsAozoraElements

object HeadingMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        node as? Element ?: return null
        if (node.tagName() != "div") return null

        val className = node.attr("class")
        val indent = className.split("_").getOrNull(1)?.toIntOrNull() ?: return null
        val child = node.children().getOrNull(0) ?: return null
        val styleName = child.attr("class")
        val style =
            when (styleName) {
                "naka-midashi" -> AozoraTextStyle.HEADING_MEDIUM
                "o-midashi" -> AozoraTextStyle.HEADING_LARGE

                else -> AozoraTextStyle.PARAGRAPH
            }
        val contentNodes = child.selectFirst(".midashi_anchor")?.childNodes() ?: return null
        val elements = contentNodes.parseAsAozoraElements()
        return Heading(
            indent = indent,
            style = style,
            elements = elements + AozoraElement.LineBreak,
        )
    }
}
