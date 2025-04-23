package me.andannn.aosora.core.parser

import me.andannn.aosora.core.parser.internal.plaintext.AozoraPlainTextParser.parseLine
import me.andannn.aosora.core.parser.internal.plaintext.AozoraPlainTextParser.parseLineAsBlock
import kotlin.test.Test
import kotlin.test.assertEquals

class AozoraParserTest {
    @Test
    fun testParseAozoraLineOptimized() {
        val sampleString = """
            ふと気が付いて見ると書生はいない。たくさんおった兄弟が一｜疋《ぴき》も見えぬ。肝心《かんじん》の母親さえ姿を隠してしまった。。
        """.trimIndent()
        val result = parseLine(sampleString)
        assertEquals(
            AozoraElement.Text("ふと気が付いて見ると書生はいない。たくさんおった兄弟が一"),
            result[0]
        )
        assertEquals(AozoraElement.Ruby("疋", ruby = "ぴき"), result[1])
        assertEquals(AozoraElement.Text("も見えぬ。"), result[2])
        assertEquals(AozoraElement.Ruby("肝心", ruby = "かんじん"), result[3])
    }

    @Test
    fun testParseBouten() {
        val sampleString = """
            傍に畏る葬式彦と緒《とも》に、いささか出鼻を挫《くじ》かれた心持ちで、に［＃「に」に傍点］組の頭常吉の言葉に先刻から耳を傾けている。
        """.trimIndent()
        val result = parseLine(sampleString)
        assertEquals(AozoraElement.Text("傍に畏る葬式彦と"), result[0])
        assertEquals(AozoraElement.Ruby("緒", ruby = "とも"), result[1])
        assertEquals(AozoraElement.Text("に、いささか出鼻を"), result[2])
        assertEquals(AozoraElement.Ruby("挫", ruby = "くじ"), result[3])
        assertEquals(AozoraElement.Text("かれた心持ちで、"), result[4])
        assertEquals(AozoraElement.Emphasis("に", EmphasisStyle.Bouten), result[5])
        assertEquals(AozoraElement.Text("組の頭常吉の言葉に先刻から耳を傾けている。"), result[6])
    }

    @Test
    fun testParseBouten2() {
        val sampleString = """
            藤吉はむっくり［＃「むっくり」に傍点］起き上った。
        """.trimIndent()
        val result = parseLine(sampleString)
        assertEquals(AozoraElement.Text("藤吉は"), result[0])
        assertEquals(AozoraElement.Emphasis("むっくり", EmphasisStyle.Bouten), result[1])
        assertEquals(AozoraElement.Text("起き上った。"), result[2])
    }

    @Test
    fun testParseAozoraBlock() {
        val sampleString = """
［＃４字下げ］第一　腹中《ふくちゅう》の新年［＃「第一　腹中の新年」は中見出し］
""".trimIndent()
        val result = parseLineAsBlock(sampleString)
        assertEquals(result.blockType, BlockType.Heading(indent = 4, style = AozoraTextStyle.HEADING_MEDIUM))
    }

    @Test
    fun testParseStringWithLinkBreak() {
        val sampleString = """
第
一 腹中《ふくちゅう》の新
年
""".trimIndent()
        println(sampleString)
        val result = parseLine(sampleString)
        assertEquals(AozoraElement.Text("第"), result[0])
        assertEquals(AozoraElement.LineBreak, result[1])
        assertEquals(AozoraElement.Text("一 "), result[2])
        assertEquals(AozoraElement.Ruby("腹中", ruby = "ふくちゅう"), result[3])
        assertEquals(AozoraElement.Text("の新"), result[4])
        assertEquals(AozoraElement.LineBreak, result[5])
        assertEquals(AozoraElement.Text("年"), result[6])
    }
}