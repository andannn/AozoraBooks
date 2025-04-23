package me.andannn.aosora.core.parser.html.matchers

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.EmphasisStyle
import me.andannn.aosora.core.parser.html.ElementMatcher
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import java.util.Locale

object EmphasisMatcher : ElementMatcher{
    override fun match(node: Node): AozoraElement? {
        node as? Element ?: return null

        val className = node.attr("class")

        if (className.lowercase(Locale.getDefault()) != "sesame_dot") {
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