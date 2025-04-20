package me.andannn.aosora.core.parser

import me.andannn.aosora.core.parser.internal.parsers.EmphasisParser
import me.andannn.aosora.core.parser.internal.parsers.HeadingParser
import me.andannn.aosora.core.parser.internal.parsers.IllustrationNotionParser
import me.andannn.aosora.core.parser.internal.parsers.IndentParser
import me.andannn.aosora.core.parser.internal.parsers.PageBreakParser
import me.andannn.aosora.core.parser.internal.parsers.RubyParser
import me.andannn.aosora.core.parser.internal.parsers.SpecificRubyParser
import kotlin.test.Test
import kotlin.test.assertEquals

class AozoraElementParserTest {

    @Test
    fun testRubyParser() {
        val matchResult = SpecificRubyParser.matchAll(
            "たくさんおった兄弟が一｜疋《ぴき》も見えぬ"
        ).first()
        val result = SpecificRubyParser.create(matchResult)
        assert(result is AozoraElement.Ruby)
        assert((result as AozoraElement.Ruby).text == "疋")
        assert(result.ruby == "ぴき")
    }

    @Test
    fun testKanjiRubyRegexParser() {
        val matchResult = RubyParser.matchAll(
            "この間おさんの三馬《さんま》を偸《ぬす》んでこの返報をしてやってから"
        ).first()
        val result = RubyParser.create(matchResult)
        assert(result is AozoraElement.Ruby)
        assert((result as AozoraElement.Ruby).text == "三馬")
        assert(result.ruby == "さんま")
    }

    @Test
    fun testIllustrationParser() {
        val matchResult = IllustrationNotionParser.matchAll(
            """
                ［＃大隈伯爵邸臺所の畫の図（fig49947_01.png、横800×縦600）入る］
            """.trimIndent()
        ).first()
        val result = IllustrationNotionParser.create(matchResult)
        assert(result is AozoraElement.Illustration)
        result as AozoraElement.Illustration
        assert(result.filename == "fig49947_01.png")
        assert(result.width == 800)
        assert(result.height == 600)
    }

    @Test
    fun testEmphasisParser() {
        val sample = """
            かれた心持ちで、に［＃「に」に傍点］組の頭常吉
        """.trimIndent()
        val matchResult = EmphasisParser.matchAll(sample).first()
        val result = EmphasisParser.create(matchResult)
        assert(result is AozoraElement.Emphasis)
    }

    @Test
    fun testIndentParser() {
        val matchResult = IndentParser.matchAll(
            "［＃１２３字下げ］"
        ).first()
        val result = IndentParser.create(matchResult)
        assert(result is AozoraElement.Indent)
        assert((result as AozoraElement.Indent).count == 123)
    }

    @Test
    fun testPageBreakParser() {
        val matchResult = PageBreakParser.matchAll(
            "［＃改ページ］"
        ).first()
        val result = PageBreakParser.create(matchResult)
        assert(result is AozoraElement.PageBreak)
    }

    @Test
    fun testHeadingParser() {
        val matchResult = HeadingParser.matchAll(
            "［＃８字下げ］春の巻［＃「春の巻」は大見出し］"
        ).first()
        assertEquals(0, matchResult.range.first)

        val result = HeadingParser.create(matchResult)
        assert(result is AozoraElement.Heading)

        val largeHeading = (result as AozoraElement.Heading)
        assertEquals("春の巻", largeHeading.text)
        assert(largeHeading.style == AozoraTextStyle.HEADING_LARGE)
        assertEquals(AozoraElement.Text("春の巻"), largeHeading.elements[0])
    }

    @Test
    fun testHeadingParser2() {
        val matchResult = HeadingParser.matchAll(
            "［＃８字下げ］第一　腹中《ふくちゅう》の新年［＃「第一　腹中の新年」は中見出し］"
        ).first()
        val result = HeadingParser.create(matchResult)
        assert(result is AozoraElement.Heading)
        val largeHeading = (result as AozoraElement.Heading)
        assertEquals("第一　腹中の新年", largeHeading.text)
        assert(largeHeading.style == AozoraTextStyle.HEADING_MEDIUM)
        assertEquals(AozoraElement.Text("第一　"), largeHeading.elements[0])
        assertEquals(AozoraElement.Ruby("腹中", ruby = "ふくちゅう"), largeHeading.elements[1])
        assertEquals(AozoraElement.Text("の新年"), largeHeading.elements[2])
    }
}