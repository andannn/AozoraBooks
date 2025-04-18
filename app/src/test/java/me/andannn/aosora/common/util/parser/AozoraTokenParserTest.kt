package me.andannn.aosora.common.util.parser

import me.andannn.aosora.common.model.AozoraString
import org.junit.Test

class AozoraTokenParserTest {

    @Test
    fun testRubyParser() {
        val matchResult =SpecificRubyParser.match(
            "たくさんおった兄弟が一｜疋《ぴき》も見えぬ",
            0
        )

        assert(matchResult != null)
        val result = SpecificRubyParser.create(matchResult!!)
        assert(result is AozoraString.Ruby)
        assert((result as AozoraString.Ruby).text == "疋")
        assert(result.ruby == "ぴき")
    }

    @Test
    fun testKanjiRubyRegexParser() {
        val matchResult = RubyParser.match(
            "この間おさんの三馬《さんま》を偸《ぬす》んでこの返報をしてやってから",
            0
        )
        assert(matchResult != null)
        val result = RubyParser.create(matchResult!!)
        assert(result is AozoraString.Ruby)
        assert((result as AozoraString.Ruby).text == "三馬")
        assert(result.ruby == "さんま")
    }
}