package me.andannn.aozora.core.parser.html.matchers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.EmphasisStyle
import me.andannn.aozora.core.parser.html.ElementMatcher
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node

object EmphasisMatcher : ElementMatcher{
    override fun match(node: Node): AozoraElement? {
        node as? Element ?: return null

        val className = node.attr("class")

        if (className.lowercase() != "sesame_dot") {
            return null
        }

        val style = when (node.tagName()) {
            "em" -> EmphasisStyle.Bouten
            "strong" -> EmphasisStyle.Strong
            else -> error("unknown emphasis style: ${node.tagName()}")
        }
        val sesameDot = node.text()
        return AozoraElement.Emphasis(sesameDot, style)
    }
}