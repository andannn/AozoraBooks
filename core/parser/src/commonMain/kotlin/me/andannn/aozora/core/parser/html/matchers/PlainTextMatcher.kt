package me.andannn.aozora.core.parser.html.matchers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.html.ElementMatcher
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode

object PlainTextMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        node as? TextNode ?: return null

        return AozoraElement.Text(node.text())
    }
}