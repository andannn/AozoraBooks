package me.andannn.aosora.core.parser.html.matchers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.html.ElementMatcher
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

object PlainTextMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        node as? TextNode ?: return null

        return AozoraElement.Text(node.text())
    }
}