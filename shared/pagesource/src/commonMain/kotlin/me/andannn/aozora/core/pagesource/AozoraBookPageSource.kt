/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.asFlow
import kotlinx.io.Buffer
import kotlinx.io.writeString
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.pagesource.BookPageSource
import me.andannn.aozora.core.pagesource.parser.DefaultAozoraBlockParser
import me.andannn.aozora.core.pagesource.parser.html.HtmlLineParser
import me.andannn.aozora.core.pagesource.parser.lineSequence
import me.andannn.aozora.core.pagesource.raw.BookInfo
import me.andannn.aozora.core.pagesource.raw.BookRawSource
import me.andannn.aozora.core.pagesource.raw.RemoteOrLocalCacheBookRawSource
import me.andannn.aozora.core.pagesource.util.validBlock

class BookPageSourceFactory : BookPageSource.Factory {
    override fun createDummySource(): BookPageSource = buildDummyBookPageSource()

    override fun createBookPageSource(
        card: AozoraBookCard,
        scope: CoroutineScope,
    ): BookPageSource = buildAozoraBookPageSource(card, scope)
}

/**
 * Book page source.
 */
private fun buildAozoraBookPageSource(
    card: AozoraBookCard,
    scope: CoroutineScope,
): BookPageSource =
    CachedLinerPageSource(
        RemoteOrLocalCacheBookRawSource(
            card,
            scope,
            dispatcher = Dispatchers.IO,
        ),
    )

fun buildDummyBookPageSource(): BookPageSource =
    CachedLinerPageSource(
        object : BookRawSource {
            private val dummyHtml: String =
                """
                        <br>
                <div class="jisage_2" style="margin-left: 2em"> <div class="jizume_10" style="width: 30em">  　巻頭の口画に掲げたるは現今上流社会台所の模範と称せらるる<ruby><rb>牛込</rb><rp>（</rp><rt>うしごめ</rt><rp>）</rp></ruby>早稲田大隈伯爵家の台所にして<ruby><rb>山本松谷</rb><rp>（</rp><rt>やまもとしょうこく</rt><rp>）</rp></ruby>氏が健腕を以て詳密に実写せし真景なり。台所は昨年の新築に成り、主人公の伯爵が和洋の料理に適用せしめんと最も苦心せられし新考案の設備にてその広さ二十五坪、<ruby><rb>半</rb><rp>（</rp><rt>なかば</rt><rp>）</rp></ruby>は<ruby><rb>板敷</rb><rp>（</rp><rt>いたじき</rt><rp>）</rp></ruby>半はセメントの土間にして天井におよそ四坪の<ruby><rb>硝子明取</rb><rp>（</rp><rt>がらすあかりと</rt><rp>）</rp></ruby>りあり。極めて清潔なると器具配置の<ruby><rb>整頓</rb><rp>（</rp><rt>せいとん</rt><rp>）</rp></ruby>せると<ruby><rb>立働</rb><rp>（</rp><rt>たちはたら</rt><rp>）</rp></ruby>きの便利なると<ruby><rb>鼠</rb><rp>（</rp><rt>ねずみ</rt><rp>）</rp></ruby>の<ruby><rb>竄入</rb><rp>（</rp><rt>ざんにゅう</rt><rp>）</rp></ruby>せざると全体の衛生的なるとはこの台所の特長なり。口画を<ruby><rb>披</rb><rp>（</rp><rt>ひら</rt><rp>）</rp></ruby>く者は土間の中央に一大ストーブの<ruby><rb>据</rb><rp>（</rp><rt>すえ</rt><rp>）</rp></ruby>られたるを見ん。これ英国より取寄せられたる<ruby><rb>瓦斯</rb><rp>（</rp><rt>がす</rt><rp>）</rp></ruby>ストーブにて高さ四尺長さ五尺幅弐尺あり、この<ruby><rb>価</rb><rp>（</rp><rt>あたえ</rt><rp>）</rp></ruby>弐百五十円なりという。ストーブの<ruby><rb>傍</rb><rp>（</rp><rt>かたわら</rt><rp>）</rp></ruby>に大小の大釜<ruby><rb>両個</rb><rp>（</rp><rt>ふたつ</rt><rp>）</rp></ruby>あり。釜の<ruby><rb>此方</rb><rp>（</rp><rt>こなた</rt><rp>）</rp></ruby>に<ruby><rb>厨人</rb><rp>（</rp><rt>ちゅうじん</rt><rp>）</rp></ruby>土間に立ちて<ruby><rb>壺</rb><rp>（</rp><rt>つぼ</rt><rp>）</rp></ruby>を棚に<ruby><rb>載</rb><rp>（</rp><rt>の</rt><rp>）</rp></ruby>せ、厨人の前方板にて<ruby><rb>囲</rb><rp>（</rp><rt>かこ</rt><rp>）</rp></ruby>いたる中に<ruby><rb>瓦斯竈</rb><rp>（</rp><rt>がすかまど</rt><rp>）</rp></ruby>三基を置く。中央の<ruby><rb>置棚</rb><rp>（</rp><rt>おきだな</rt><rp>）</rp></ruby>に野菜類の<ruby><rb>堆</rb><rp>（</rp><rt>うずたか</rt><rp>）</rp></ruby>く<ruby><rb>籠</rb><rp>（</rp><rt>かご</rt><rp>）</rp></ruby>に盛られたるは同邸の一名物と称せらるる温室仕立の野菜なり。三月に<ruby><rb>瓜</rb><rp>（</rp><rt>うり</rt><rp>）</rp></ruby>あり、四月に<ruby><rb>茄子</rb><rp>（</rp><rt>なす</rt><rp>）</rp></ruby>あり、根葉果茎一として食卓の珍ならざるはなし。下働きの女中、給仕役の少女、各その職を<ruby><rb>執</rb><rp>（</rp><rt>と</rt><rp>）</rp></ruby>りて事に当る。人も美しく、<ruby><rb>四辺</rb><rp>（</rp><rt>あたり</rt><rp>）</rp></ruby>も清潔なり。この台所に<ruby><rb>入</rb><rp>（</rp><rt>い</rt><rp>）</rp></ruby>る者は<ruby><rb>先</rb><rp>（</rp><rt>ま</rt><rp>）</rp></ruby>ず<ruby><rb>眉目</rb><rp>（</rp><rt>びもく</rt><rp>）</rp></ruby>に明快なるを覚ゆべし。  <br>  　この台所にては毎日平均五十人前以上の食事を<ruby><rb>調</rb><rp>（</rp><rt>ととの</rt><rp>）</rp></ruby>う。百人二百人の<ruby><rb>賓客</rb><rp>（</rp><rt>ひんかく</rt><rp>）</rp></ruby>ありても千人二千人の立食を作るも<ruby><rb>皆</rb><rp>（</rp><rt>み</rt><rp>）</rp></ruby>なここにて事足るなり。伯爵家にては大概各日位に西洋料理を調えらる。和洋の料理、この設備に<ruby><rb>拠</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>れば手に応じて成り、また何の不便不足を感ずる所なし。この台所のかくまで便宜に適したるはストーブにも竈にも瓦斯を用いたるがためなり。瓦斯なるために<ruby><rb>薪炭</rb><rp>（</rp><rt>まきすみ</rt><rp>）</rp></ruby>の置場を要せず、<ruby><rb>烟突</rb><rp>（</rp><rt>えんとつ</rt><rp>）</rp></ruby>を要せず、鍋釜の底の<ruby><rb>煤</rb><rp>（</rp><rt>すす</rt><rp>）</rp></ruby>に汚れる<ruby><rb>憂</rb><rp>（</rp><rt>うれい</rt><rp>）</rp></ruby>もなく、急を要する時もマッチ一本にて自在の火力を<ruby><rb>得</rb><rp>（</rp><rt>う</rt><rp>）</rp></ruby>べし。物を<ruby><rb>炙</rb><rp>（</rp><rt>あぶ</rt><rp>）</rp></ruby>り物を<ruby><rb>煮</rb><rp>（</rp><rt>に</rt><rp>）</rp></ruby>るも火力平均するがため少しくその使用法に<ruby><rb>馴</rb><rp>（</rp><rt>な</rt><rp>）</rp></ruby>るれば<ruby><rb>仕損</rb><rp>（</rp><rt>しそん</rt><rp>）</rp></ruby>ずる<ruby><rb>気支</rb><rp>（</rp><rt>きづかい</rt><rp>）</rp></ruby>なし。費用は薪炭の時代に一日壱円五十一銭を要せしが今は瓦斯代九十五銭を要するのみ。即ち一日に五十六銭の利あり。<ruby><rb>然</rb><rp>（</rp><rt>しか</rt><rp>）</rp></ruby>れども瓦斯の使用は軽便と清潔と人の手数とを省く点において費用の減少よりもなお<ruby><rb>大</rb><rp>（</rp><rt>おおい</rt><rp>）</rp></ruby>なる利益あり。  <br>  　文明の生活をなさんものは文明の台所を要す。和洋の料理を<ruby><rb>為</rb><rp>（</rp><rt>な</rt><rp>）</rp></ruby>さんものはよろしくこの新考案を学ぶべし。  <br> </div></div><br>
                """.trimIndent()

            override fun getRawSource() =
                Buffer()
                    .apply {
                        writeString(dummyHtml)
                    }.lineSequence()
                    .map { DefaultAozoraBlockParser(HtmlLineParser).parseLineAsBlock(it) }
                    .validBlock()
                    .asFlow()

            override suspend fun getImageUriByPath(path: String) = TODO("Not yet implemented")

            override suspend fun getBookInfo() =
                BookInfo(
                    title = "dummy",
                    subtitle = null,
                    author = "dummy",
                    bibliographicalInformation = "dummy",
                    tableOfContentList = emptyList(),
                )
        },
    )
