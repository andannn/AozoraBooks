/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.pagesource.parser.html.matchers.EmphasisMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.HeadingMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.ImageMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.LineBreakMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.PlainTextMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.RubyMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.SpecialParagraphMatcher

/**
 * html node element matcher
 */
internal interface ElementMatcher {
    fun match(node: Node): MatchResult
}

internal sealed interface MatchResult {
    sealed interface Matched : MatchResult

    data class ElementMatched(
        val element: AozoraElement,
    ) : Matched

    data class BlockMatched(
        val elements: List<AozoraElement>,
        val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH,
        val indent: Int = 0,
        val maxLength: Int? = null,
        val headingLevel: Int = 0,
    ) : Matched

    object NotMatched : MatchResult
}

/**
 * parse string to html nodes
 */
internal fun String.parseAsHtmlNodes(): List<Node> = Ksoup.parseBodyFragment(this).body().childNodes()

internal fun List<Node>.matchAsMatchResults(): List<MatchResult.Matched> =
    mapNotNull { node ->
        PARSERS.firstNotNullOfOrNull { it.match(node).takeIf { it is MatchResult.Matched } }
    }.filterIsInstance<MatchResult.Matched>()

private val PARSERS =
    listOf(
        PlainTextMatcher,
        RubyMatcher,
        ImageMatcher,
        EmphasisMatcher,
        LineBreakMatcher,
        HeadingMatcher,
        SpecialParagraphMatcher,
    )
