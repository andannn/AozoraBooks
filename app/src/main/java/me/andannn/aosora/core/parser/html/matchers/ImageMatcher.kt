package me.andannn.aosora.core.parser.html.matchers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.html.ElementMatcher
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

object ImageMatcher: ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        node as? Element ?: return null
        if (node.tagName() != "img") {
            return null
        }
        val width = node.attr("width").toIntOrNull() ?: return null
        val height = node.attr("height").toIntOrNull() ?: return null
        val src = node.attr("src") ?: return null
        return AozoraElement.Illustration(src, width, height)
    }
}