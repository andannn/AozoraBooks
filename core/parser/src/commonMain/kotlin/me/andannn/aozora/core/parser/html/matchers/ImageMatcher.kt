package me.andannn.aozora.core.parser.html.matchers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.html.ElementMatcher
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node

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