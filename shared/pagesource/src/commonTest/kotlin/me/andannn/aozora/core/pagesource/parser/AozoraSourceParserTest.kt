/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.EmphasisStyle
import me.andannn.aozora.core.pagesource.parser.html.HtmlLineParser
import me.andannn.aozora.core.pagesource.parser.html.MatchResult
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ktlint:standard:max-line-length")
class AozoraSourceParserTest {
    private val sample1: String
        get() = "小山の妻君は<br />"
    private val sample2: String
        get() =
            "ふと気が付いて見ると書生はいない。たくさんおった兄弟が一<ruby><rb>疋</rb><rp>（</rp><rt>ぴき</rt><rp>）</rp></ruby>" +
                "も見えぬ。<ruby><rb>肝心</rb><rp>（</rp><rt>かんじん</rt><rp>）</rp></ruby>の母親"
    private val sample3: String
        get() = "藤吉は<strong class=\"SESAME_DOT\">むっくり</strong>起き上った。"
    private val parser: AozoraLineParser
        get() = HtmlLineParser

    private fun String.asRawLine() = RawLine(0L, 0L, this)

    @Test
    fun testParseStringWithLinkBreak() {
        val sampleString = sample1
        val result = parser.parseLine(sampleString.asRawLine())
        assertEquals(MatchResult.ElementMatched(AozoraElement.Text("小山の妻君は")), result[0])
        assertEquals(MatchResult.ElementMatched(AozoraElement.LineBreak), result[1])
    }

    @Test
    fun testParseAozoraLineOptimized() {
        val sampleString = sample2
        val result = parser.parseLine(sampleString.asRawLine())
        assertEquals(
            MatchResult.ElementMatched(
                AozoraElement.Text("ふと気が付いて見ると書生はいない。たくさんおった兄弟が一"),
            ),
            result[0],
        )
        assertEquals(
            MatchResult.ElementMatched(
                AozoraElement.Ruby("疋", ruby = "ぴき"),
            ),
            result[1],
        )
        assertEquals(
            MatchResult.ElementMatched(
                AozoraElement.Text("も見えぬ。"),
            ),
            result[2],
        )
        assertEquals(
            MatchResult.ElementMatched(
                AozoraElement.Ruby("肝心", ruby = "かんじん"),
            ),
            result[3],
        )
    }

    @Test
    fun testParseBouten2() {
        val sampleString = sample3
        val result = parser.parseLine(sampleString.asRawLine())
        assertEquals(
            MatchResult.ElementMatched(
                AozoraElement.Text("藤吉は"),
            ),
            result[0],
        )
        assertEquals(
            MatchResult.ElementMatched(
                AozoraElement.Emphasis(
                    text = "むっくり",
                    emphasisStyle = EmphasisStyle.Strong,
                ),
            ),
            result[1],
        )
        assertEquals(
            MatchResult.ElementMatched(
                AozoraElement.Text("起き上った。"),
            ),
            result[2],
        )
    }
}
