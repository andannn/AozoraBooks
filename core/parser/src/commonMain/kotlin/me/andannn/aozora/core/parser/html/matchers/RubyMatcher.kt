package me.andannn.aozora.core.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.html.ElementMatcher

object RubyMatcher : ElementMatcher {
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
