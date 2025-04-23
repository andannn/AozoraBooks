package me.andannn.aosora.core.parser.html

import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.html.matchers.EmphasisMatcher
import me.andannn.aosora.core.parser.html.matchers.HeadingMatcher
import me.andannn.aosora.core.parser.html.matchers.ImageMatcher
import me.andannn.aosora.core.parser.html.matchers.LineBreakMatcher
import me.andannn.aosora.core.parser.html.matchers.PlainTextMatcher
import me.andannn.aosora.core.parser.html.matchers.RubyMatcher
import org.jsoup.Jsoup
import org.jsoup.nodes.Node

/**
 * html node element matcher
 */
interface ElementMatcher {
    fun match(node: Node): AozoraElement?
}

/**
 * parse string to html nodes
 */
fun String.parseAsHtmlNodes(): List<Node> = Jsoup.parseBodyFragment(this).body().childNodes()

fun List<Node>.parseAsAozoraElements(): List<AozoraElement> =
    mapNotNull { node ->
        PARSERS.firstNotNullOfOrNull { it.match(node) }
    }

private val PARSERS = listOf(
    PlainTextMatcher,
    RubyMatcher,
    ImageMatcher,
    EmphasisMatcher,
    LineBreakMatcher,
    HeadingMatcher
)
