package me.andannn.aozora.core.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.html.ElementMatcher

object LineBreakMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        node as? Element ?: return null
        if (node.tagName() != "br") {
            return null
        }
        return AozoraElement.LineBreak
    }
}
