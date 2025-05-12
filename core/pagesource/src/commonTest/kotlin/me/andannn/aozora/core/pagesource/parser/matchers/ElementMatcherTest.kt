/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.matchers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraElement.SpecialParagraph
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.data.common.EmphasisStyle
import me.andannn.aozora.core.pagesource.parser.html.matchers.EmphasisMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.HeadingMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.ImageMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.LineBreakMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.PlainTextMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.RubyMatcher
import me.andannn.aozora.core.pagesource.parser.html.matchers.SpecialParagraphMatcher
import me.andannn.aozora.core.pagesource.parser.html.parseAsHtmlNodes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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
            RubyMatcher.match(
                "<ruby><rb>饒</rb><rp>（</rp><rt>ゆたけ</rt><rp>）</rp></ruby>"
                    .parseAsHtmlNodes()
                    .first(),
            ),
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
            EmphasisMatcher.match(
                "<em class=\"sesame_dot\">せびられ</em>".parseAsHtmlNodes().first(),
            ),
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

    @Test
    fun testHeaderMatcher2() {
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
                    """
<div class="jisage_6" style="margin-left: 6em"><h4 class="dogyo-naka-midashi"><a class="midashi_anchor" id="midashi10">序詞</a></h4>（祇園精舎）</div>                    
                    """.trimIndent()
                ).parseAsHtmlNodes().first(),
            ),
        )
    }

    @Test
    fun testSpecialParagraphMatcher() {
        val result =
            SpecialParagraphMatcher.match(
                (
                    """
<div class="jisage_4" style="margin-left: 4em">
<div class="jizume_27" style="width: 27em">
　<ruby><rb>祇園精舎</rb><rp>（</rp><rt>ぎおんしょうじゃ</rt><rp>）</rp></ruby>の鐘の声、諸行無常の<ruby><rb>響</rb><rp>（</rp><rt>ひびき</rt><rp>）</rp></ruby>あり。<ruby><rb>娑羅双樹</rb><rp>（</rp><rt>しゃらそうじゅ</rt><rp>）</rp></ruby>の花の色、<ruby><rb>盛者</rb><rp>（</rp><rt>しょうじゃ</rt><rp>）</rp></ruby>必衰の<ruby><rb>理</rb><rp>（</rp><rt>ことわり</rt><rp>）</rp></ruby>をあらわす。おごれる人も久しからず、唯、春の夜の夢のごとし。<ruby><rb>猛</rb><rp>（</rp><rt>たけ</rt><rp>）</rp></ruby>きものもついにはほろびぬ、<ruby><rb>偏</rb><rp>（</rp><rt>ひとえ</rt><rp>）</rp></ruby>に風の前の<ruby><rb>塵</rb><rp>（</rp><rt>ちり</rt><rp>）</rp></ruby>に同じ。<br>
</div>
</div>    
                    """.trimIndent()
                ).parseAsHtmlNodes().first(),
            )
        assertIs<SpecialParagraph>(result)
        assertEquals(4, result.indent)
        assertEquals(27, result.maxLength)
    }

    @Test
    fun testSpecialParagraphMatcher2() {
        val result =
            SpecialParagraphMatcher.match(
                (
                    """
<div class="jisage_2" style="margin-left: 2em">
<ruby><rb>仏</rb><rp>（</rp><rt>ほとけ</rt><rp>）</rp></ruby>も昔は凡夫なり　我等も遂には仏なり。<br>
<ruby><rb>何</rb><rp>（</rp><rt>いず</rt><rp>）</rp></ruby>れも<ruby><rb>仏性</rb><rp>（</rp><rt>ぶっしょう</rt><rp>）</rp></ruby><ruby><rb>具</rb><rp>（</rp><rt>ぐ</rt><rp>）</rp></ruby>せる身を　<ruby><rb>隔</rb><rp>（</rp><rt>へだ</rt><rp>）</rp></ruby>つるのみこそ悲しけれ。<br>
</div> 
                    """.trimIndent()
                ).parseAsHtmlNodes().first(),
            )
        assertIs<SpecialParagraph>(result)
        assertEquals(2, result.indent)
        assertEquals(0, result.maxLength)
    }
}
