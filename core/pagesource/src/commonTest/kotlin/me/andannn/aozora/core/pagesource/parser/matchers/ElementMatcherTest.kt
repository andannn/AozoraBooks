/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.matchers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.data.common.EmphasisStyle
import me.andannn.aozora.core.pagesource.parser.html.matchers.EmphasisMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.HeadingMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.ImageMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.LineBreakMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.PlainTextMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.RubyMatcher
import me.andannn.aozora.core.pagesource.parser.html.parseAsHtmlNodes
import kotlin.test.Test
import kotlin.test.assertEquals

class ElementMatcherTest {
    @Test
    fun testTextNodeMatcher() {
        assertEquals(
            AozoraElement.Text("AAA"),
            PlainTextMatcher.match("AAA".parseAsHtmlNodes().first()),
        )
    }

    @Test
    fun testRubyNodeMatcher() {
        assertEquals(
            AozoraElement.Ruby("饒", "ゆたけ"),
            RubyMatcher.match("<ruby><rb>饒</rb><rp>（</rp><rt>ゆたけ</rt><rp>）</rp></ruby>".parseAsHtmlNodes().first()),
        )
    }

    @Test
    fun testImageNodeMatcher() {
        assertEquals(
            AozoraElement.Illustration(filename = "fig49947_04.png", width = 311, height = 235),
            ImageMatcher.match(
                "<img class=\"illustration\" width=\"311\" height=\"235\" src=\"fig49947_04.png\" alt=\"「シチュウ鍋の図」のキャプション付きの図\" /><br />\n>"
                    .parseAsHtmlNodes()
                    .first(),
            ),
        )
    }

    @Test
    fun testLineBreak() {
        assertEquals(
            AozoraElement.LineBreak,
            LineBreakMatcher.match("<br />".parseAsHtmlNodes().first()),
        )
    }

    @Test
    fun testEmphasisMatcher() {
        assertEquals(
            AozoraElement.Emphasis("せびられ", emphasisStyle = EmphasisStyle.Bouten),
            EmphasisMatcher.match("<em class=\"sesame_dot\">せびられ</em>".parseAsHtmlNodes().first()),
        )
    }

    @Test
    fun testHeaderMatcher() {
        assertEquals(
            AozoraElement.Heading(
                indent = 4,
                style = AozoraTextStyle.HEADING_MEDIUM,
                headingLevel = 4,
                elements =
                    listOf(
                        AozoraElement.Text("第一　"),
                        AozoraElement.Ruby("腹中", "ふくちゅう"),
                        AozoraElement.Text("の新年"),
                        AozoraElement.LineBreak,
                    ),
            ),
            HeadingMatcher.match(
                (
                    "<div class=\"jisage_4\" style=\"margin-left: 4em\">" +
                        "<h4 class=\"naka-midashi\"><a class=\"midashi_anchor\" id=\"midashi120\">" +
                        "第一　<ruby><rb>腹中</rb><rp>（</rp><rt>ふくちゅう</rt><rp>）</rp></ruby>の新年</a></h4></div>\n"
                ).parseAsHtmlNodes().first(),
            ),
        )
    }
}
