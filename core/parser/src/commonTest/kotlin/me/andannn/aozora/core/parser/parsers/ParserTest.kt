package me.andannn.aozora.core.parser.parsers

import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.parser.plaintext.parsers.HeadingParser
import me.andannn.aozora.core.parser.plaintext.parsers.IllustrationNotionParser
import me.andannn.aozora.core.parser.plaintext.parsers.IndentParser
import me.andannn.aozora.core.parser.plaintext.parsers.LineBreakParser
import me.andannn.aozora.core.parser.plaintext.parsers.PageBreakParser
import me.andannn.aozora.core.parser.plaintext.parsers.RubyParser
import me.andannn.aozora.core.parser.plaintext.parsers.SpecificRubyParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ParserTest {
    @Test
    fun testRubyParser() {
        val matchResult =
            SpecificRubyParser
                .matchAll(
                    "たくさんおった兄弟が一｜疋《ぴき》も見えぬ",
                ).first()
        val result = SpecificRubyParser.create(matchResult)
        assertIs<AozoraElement.Ruby>(result)
        assertEquals("疋", result.text)
        assertEquals("ぴき", result.ruby)
    }

    @Test
    fun testKanjiRubyRegexParser() {
        val matchResult =
            RubyParser
                .matchAll(
                    "この間おさんの三馬《さんま》を偸《ぬす》んでこの返報をしてやってから",
                ).first()
        val result = RubyParser.create(matchResult)
        assertIs<AozoraElement.Ruby>(result)
        assertEquals("三馬", result.text)
        assertEquals("さんま", result.ruby)
    }

    @Test
    fun testIllustrationParser() {
        val matchResult =
            IllustrationNotionParser
                .matchAll(
                    """
                    ［＃大隈伯爵邸臺所の畫の図（fig49947_01.png、横800×縦600）入る］
                    """.trimIndent(),
                ).first()
        val result = IllustrationNotionParser.create(matchResult)
        assertIs<AozoraElement.Illustration>(result)
        assertEquals("fig49947_01.png", result.filename)
        assertEquals(800, result.width)
        assertEquals(600, result.height)
    }
// TODO: Fix Emphasis Test
//    @Test
//    fun testEmphasisParser() {
//        val sample = """
//            かれた心持ちで、に［＃「に」に傍点］組の頭常吉
//        """.trimIndent()
//        val matchResult = EmphasisParser.matchAll(sample).first()
//        val result = EmphasisParser.create(matchResult)
//        assertIs<AozoraElement.Emphasis>(result)
//    }

    @Test
    fun testIndentParser() {
        val matchResult =
            IndentParser
                .matchAll(
                    "［＃１２３字下げ］",
                ).first()
        val result = IndentParser.create(matchResult)
        assertIs<AozoraElement.Indent>(result)
        assertEquals(123, result.count)
    }

    @Test
    fun testPageBreakParser() {
        val matchResult =
            PageBreakParser
                .matchAll(
                    "［＃改ページ］",
                ).first()
        val result = PageBreakParser.create(matchResult)
        assertIs<AozoraElement.PageBreak>(result)
    }

    @Test
    fun testHeadingParser() {
        val matchResult =
            HeadingParser
                .matchAll(
                    "［＃８字下げ］春の巻［＃「春の巻」は大見出し］",
                ).first()
        assertEquals(0, matchResult.range.first)

        val result = HeadingParser.create(matchResult)
        assertIs<AozoraElement.Heading>(result)

        assertEquals("春の巻", result.text)
        assertEquals(AozoraTextStyle.HEADING_LARGE, result.style)
        assertEquals(AozoraElement.Text("春の巻"), result.elements[0])
    }

    @Test
    fun testHeadingParser2() {
        val matchResult =
            HeadingParser
                .matchAll(
                    "［＃８字下げ］第一　腹中《ふくちゅう》の新年［＃「第一　腹中の新年」は中見出し］",
                ).first()
        val result = HeadingParser.create(matchResult)
        assertIs<AozoraElement.Heading>(result)
        assertEquals("第一　腹中の新年", result.text)
        assertEquals(AozoraTextStyle.HEADING_MEDIUM, result.style)
        assertEquals(AozoraElement.Text("第一　"), result.elements[0])
        assertEquals(AozoraElement.Ruby("腹中", ruby = "ふくちゅう"), result.elements[1])
        assertEquals(AozoraElement.Text("の新年"), result.elements[2])
    }

    @Test
    fun testLineBreakParser() {
        val matchResult =
            LineBreakParser
                .matchAll(
                    "abc\nd\nef",
                ).toList()
        assertEquals(2, matchResult.size)
        assertEquals(3..3, matchResult[0].range)
        assertEquals(5..5, matchResult[1].range)
        val result = LineBreakParser.create(matchResult[0])
        assertIs<AozoraElement.LineBreak>(result)
    }
}
