/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.parser.html.HtmlLineParser
import kotlin.test.Test
import kotlin.test.assertEquals

class AozoraBlockParserTest {
    private val parser: AozoraBlockParser = DefaultAozoraBlockParser(HtmlLineParser)

    private val sampleString1: String
        get() =
            "<div class=\"jisage_4\" style=\"margin-left: 4em\"><h4 class=\"naka-midashi\">" +
                "<a class=\"midashi_anchor\" id=\"midashi120\">" +
                "第一　<ruby><rb>腹中</rb><rp>（</rp><rt>ふくちゅう</rt><rp>）</rp></ruby>の新年</a></h4></div></br>"

    private fun String.asRawLine() = RawLine(0L, 0L, this)

    @Test
    fun testParseAozoraBlock() {
        val result = parser.parseLineAsBlock(sampleString1.asRawLine())
        assertEquals(
            AozoraBlock.Heading(
                indent = 4,
                textStyle = AozoraTextStyle.HEADING_MEDIUM,
                blockIndex = 0,
                elements =
                    listOf(
                        AozoraElement.Text(text = "第一　"),
                        AozoraElement.Ruby(text = "腹中", ruby = "ふくちゅう"),
                        AozoraElement.Text(text = "の新年"),
                        AozoraElement.LineBreak,
                    ),
            ),
            result,
        )
    }
}
