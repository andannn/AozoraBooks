package me.andannn.aozora.core.parser

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.html.HtmlLineParser
import me.andannn.aozora.core.parser.plaintext.PlainTextLineParser
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class AozoraSourceParserTest {
    abstract val parser: AozoraLineParser

    abstract val sample1: String
    abstract val sample2: String
    abstract val sample3: String

    private fun String.asRawLine() = RawLine(0L, 0L, this)

    @Test
    fun testParseStringWithLinkBreak() {
        val sampleString = sample1
        val result = parser.parseLine(sampleString.asRawLine())
        assertEquals(AozoraElement.Text("小山の妻君は"), result[0])
        assertEquals(AozoraElement.LineBreak, result[1])
    }

    @Test
    fun testParseAozoraLineOptimized() {
        val sampleString = sample2
        val result = parser.parseLine(sampleString.asRawLine())
        assertEquals(
            AozoraElement.Text("ふと気が付いて見ると書生はいない。たくさんおった兄弟が一"),
            result[0],
        )
        assertEquals(AozoraElement.Ruby("疋", ruby = "ぴき"), result[1])
        assertEquals(AozoraElement.Text("も見えぬ。"), result[2])
        assertEquals(AozoraElement.Ruby("肝心", ruby = "かんじん"), result[3])
    }
// TODO: FIX Emphasis Test
//    @Test
//    fun testParseBouten2() {
//        val sampleString = sample3
//        val result = parser.parseLine(sampleString.asRawLine())
//        assertEquals(AozoraElement.Text("藤吉は"), result[0])
//        assertIs<AozoraElement.Emphasis>(result[1])
//        assertEquals(AozoraElement.Text("起き上った。"), result[2])
//    }
}

class HtmlSourceParserTest : AozoraSourceParserTest() {
    override val sample1: String
        get() = "小山の妻君は<br />"
    override val sample2: String
        get() = "ふと気が付いて見ると書生はいない。たくさんおった兄弟が一<ruby><rb>疋</rb><rp>（</rp><rt>ぴき</rt><rp>）</rp></ruby>も見えぬ。<ruby><rb>肝心</rb><rp>（</rp><rt>かんじん</rt><rp>）</rp></ruby>の母親"
    override val sample3: String
        get() = "藤吉は<strong class=\"SESAME_DOT\">むっくり</strong>起き上った。"
    override val parser: AozoraLineParser
        get() = HtmlLineParser
}

class PlainTextParserTest : AozoraSourceParserTest() {
    override val parser: AozoraLineParser
        get() = PlainTextLineParser
    override val sample1: String
        get() = "小山の妻君は\n"
    override val sample2: String
        get() = "ふと気が付いて見ると書生はいない。たくさんおった兄弟が一｜疋《ぴき》も見えぬ。肝心《かんじん》の母親"
    override val sample3: String
        get() = "藤吉はむっくり［＃「むっくり」に傍点］起き上った。"
}
