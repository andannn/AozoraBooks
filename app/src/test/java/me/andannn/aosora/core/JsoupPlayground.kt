package me.andannn.aosora.core

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jsoup.Jsoup
import kotlin.test.BeforeTest
import kotlin.test.Test

class JsoupPlayground {
//    private lateinit var bookSource: BookSource
    private val path = Path("src/test/resources/html/test.html")
    @BeforeTest
    fun setUp() {
//        bookSource = BookSource(Paths.get("src/test/resources/html/test.html"))
    }

    @Test
    fun jsuop() {
        val buffer = SystemFileSystem.source(path).buffered()
//        println(buffer.readString())
//        val document = Jsoup.parse(buffer.readString())
//        val element = document.selectFirst(".main_text")!!
//        element.childNodes().forEach {
//            println(it)
//        }

        val doc = Jsoup.parseBodyFragment("　小説なお食品のごとし。味佳なるも滋養分なきものあり、味淡なるも滋養分<ruby><rb>饒</rb><rp>（</rp><rt>ゆたけ</rt><rp>）</rp></ruby>きものあり、余は常に後者を<ruby><rb>執</rb><rp>（</rp><rt>と</rt><rp>）</rp></ruby>りていささか世人に益せん事を<ruby><rb>想</rb><rp>（</rp><rt>おも</rt><rp>）</rp></ruby>う。<ruby><rb>然</rb><rp>（</rp><rt>しか</rt><rp>）</rp></ruby>れども小説中に料理法を<ruby><rb>点綴</rb><rp>（</rp><rt>てんてい</rt><rp>）</rp></ruby>するはその一致せざること懐石料理に牛豚の肉を盛るごとし。<ruby><rb>厨人</rb><rp>（</rp><rt>ちゅうじん</rt><rp>）</rp></ruby>の労苦尋常に<ruby><rb>超</rb><rp>（</rp><rt>こ</rt><rp>）</rp></ruby>えて口にするもの味を感ぜざるべし。ただ世間の食道楽者流<ruby><rb>酢豆腐</rb><rp>（</rp><rt>すどうふ</rt><rp>）</rp></ruby>を<ruby><rb>嗜</rb><rp>（</rp><rt>たしな</rt><rp>）</rp></ruby>み塩辛を<ruby><rb>嘗</rb><rp>（</rp><rt>な</rt><rp>）</rp></ruby>むるの<ruby><rb>物好</rb><rp>（</rp><rt>ものずき</rt><rp>）</rp></ruby>あらばまた余が小説の新味を喜ぶものあらん。食物の滋養分は<ruby><rb>能</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>くこれを消化して<ruby><rb>而</rb><rp>（</rp><rt>しかし</rt><rp>）</rp></ruby>て吸収せざれば人体の用を成さず。知らず余が小説よく読者に消化吸収せらるるや<ruby><rb>否</rb><rp>（</rp><rt>いな</rt><rp>）</rp></ruby>や。<br />\n")
        doc.body().childNodes().forEach {
            it.childNodes()
            println()
        }
    }
}