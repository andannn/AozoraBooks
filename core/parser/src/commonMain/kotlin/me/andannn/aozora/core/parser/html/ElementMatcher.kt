/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.parser.html

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.html.matchers.EmphasisMatcher
import me.andannn.aozora.core.parser.html.matchers.HeadingMatcher
import me.andannn.aozora.core.parser.html.matchers.ImageMatcher
import me.andannn.aozora.core.parser.html.matchers.LineBreakMatcher
import me.andannn.aozora.core.parser.html.matchers.PlainTextMatcher
import me.andannn.aozora.core.parser.html.matchers.RubyMatcher

/**
 * html node element matcher
 */
interface ElementMatcher {
    fun match(node: Node): AozoraElement?
}

/**
 * parse string to html nodes
 */
fun String.parseAsHtmlNodes(): List<Node> = Ksoup.parseBodyFragment(this).body().childNodes()

fun List<Node>.parseAsAozoraElements(): List<AozoraElement> =
    mapNotNull { node ->
        PARSERS.firstNotNullOfOrNull { it.match(node) }
    }

private val PARSERS =
    listOf(
        PlainTextMatcher,
        RubyMatcher,
        ImageMatcher,
        EmphasisMatcher,
        LineBreakMatcher,
        HeadingMatcher,
    )
