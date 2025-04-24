package me.andannn.aosora.core.parser.html.matchers

import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.parser.html.ElementMatcher
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

object RubyMatcher: ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        node as? Element ?: return null
        if (node.tagName() != "ruby") {
            return null
        }
        val text = node.select("rb").text()
        val rt = node.select("rt").text()
        return AozoraElement.Ruby(text, rt)
    }
}