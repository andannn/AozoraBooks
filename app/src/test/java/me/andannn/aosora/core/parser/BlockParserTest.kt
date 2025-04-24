package me.andannn.aosora.core.parser

import me.andannn.aosora.core.common.model.AozoraTextStyle
import me.andannn.aosora.core.common.model.BlockType
import me.andannn.aosora.core.parser.html.HtmlLineParser
import me.andannn.aosora.core.parser.plaintext.PlainTextLineParser
import org.junit.Test
import kotlin.test.assertEquals

abstract class BlockParserTest {
    abstract val parser: AozoraBlockParser

    abstract val sampleString1: String

    @Test
    fun testParseAozoraBlock() {
        val result = parser.parseLineAsBlock(sampleString1)
        assertEquals(result.blockType, BlockType.Heading(indent = 4, style = AozoraTextStyle.HEADING_MEDIUM))
    }
}

class HtmlBlockParserTest : BlockParserTest() {
    override val parser: AozoraBlockParser
        get() = createBlockParser(HtmlLineParser)
    override val sampleString1: String
        get() = "<div class=\"jisage_4\" style=\"margin-left: 4em\"><h4 class=\"naka-midashi\"><a class=\"midashi_anchor\" id=\"midashi120\">第一　<ruby><rb>腹中</rb><rp>（</rp><rt>ふくちゅう</rt><rp>）</rp></ruby>の新年</a></h4></div>"
}

class PlainTextBlockParserTest : BlockParserTest() {
    override val parser: AozoraBlockParser
        get() = createBlockParser(PlainTextLineParser)
    override val sampleString1: String
        get() = "［＃４字下げ］第一　腹中《ふくちゅう》の新年［＃「第一　腹中の新年」は中見出し］"
}