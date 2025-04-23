package me.andannn.aosora.core.parser.html.matchers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.html.ElementMatcher
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

object LineBreakMatcher: ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        node as? Element ?: return null
        if (node.tagName() != "br") {
            return null
        }
        return AozoraElement.LineBreak
    }
}