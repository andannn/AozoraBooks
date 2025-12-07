/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.parser.html.HtmlLineParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AozoraBlockParserTest {
    private val parser: AozoraBlockParser = DefaultAozoraBlockParser(HtmlLineParser)

    private val sampleString1: String
        get() =
            "<div class=\"jisage_4\" style=\"margin-left: 4em\"><h4 class=\"naka-midashi\">" +
                "<a class=\"midashi_anchor\" id=\"midashi120\">" +
                "第一　<ruby><rb>腹中</rb><rp>（</rp><rt>ふくちゅう</rt><rp>）</rp></ruby>の新年</a></h4></div></br>"

    private val sampleString2: String
        get() =
            """
            <div class="jisage_4" style="margin-left: 4em">
            <div class="jizume_27" style="width: 27em">
            　<ruby><rb>祇園精舎</rb><rp>（</rp><rt>ぎおんしょうじゃ</rt><rp>）</rp></ruby>の鐘の声、諸行無常の<ruby><rb>響</rb><rp>（</rp><rt>ひびき</rt><rp>）</rp></ruby>あり。<ruby><rb>娑羅双樹</rb><rp>（</rp><rt>しゃらそうじゅ</rt><rp>）</rp></ruby>の花の色、<ruby><rb>盛者</rb><rp>（</rp><rt>しょうじゃ</rt><rp>）</rp></ruby>必衰の<ruby><rb>理</rb><rp>（</rp><rt>ことわり</rt><rp>）</rp></ruby>をあらわす。おごれる人も久しからず、唯、春の夜の夢のごとし。<ruby><rb>猛</rb><rp>（</rp><rt>たけ</rt><rp>）</rp></ruby>きものもついにはほろびぬ、<ruby><rb>偏</rb><rp>（</rp><rt>ひとえ</rt><rp>）</rp></ruby>に風の前の<ruby><rb>塵</rb><rp>（</rp><rt>ちり</rt><rp>）</rp></ruby>に同じ。<br>
            </div>
            </div><br>
            """.trimIndent()

    private fun String.asRawLine() = RawLine(0L, 0L, this)

    @Test
    fun testParseAozoraBlock() {
        val result = parser.parseLineAsBlock(sampleString1.asRawLine()).first()
        assertEquals(
            AozoraBlock.TextBlock(
                indent = 4,
                textStyle = AozoraTextStyle.HEADING_MEDIUM,
                blockIndex = 0,
                elements =
                    listOf(
                        AozoraElement.Text(text = "第一　"),
                        AozoraElement.Ruby(text = "腹中", ruby = "ふくちゅう"),
                        AozoraElement.Text(text = "の新年"),
                    ),
            ),
            result,
        )
    }

    @Test
    fun testParseAozoraBlock2() {
        val result = parser.parseLineAsBlock(sampleString2.asRawLine()).first()
        assertIs<AozoraBlock.TextBlock>(result)
        assertEquals(4, result.indent)
        assertEquals(27, result.maxCharacterPerLine)
    }
}
