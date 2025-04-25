package me.andannn.aosora.core.pagesource

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.FontStyle
import me.andannn.aosora.core.common.model.FontType
import me.andannn.aosora.core.common.model.Line
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pagesource.page.generatePageFlow
import me.andannn.aosora.core.pagesource.page.generatePageSequence
import me.andannn.aosora.core.parser.createBlockParser
import me.andannn.aosora.core.parser.html.HtmlLineParser

fun createSimpleDummyBookPageSource(meta: PageMetaData): BookPageSource<AozoraPage> {
    val pageList = mutableListOf<AozoraPage>()
    return object : BookPageSource<AozoraPage> {
        private fun pageSource() = sequence {
            generatePageSequence(
                aozoraBlockParser = createBlockParser(HtmlLineParser),
                lineSequence = dummyHtml.lineSequence(),
                meta = meta
            )
        }

        override val pagerSnapShotFlow: Flow<PagerSnapShot<AozoraPage>> =
            pageSource().asFlow().map {
                pageList.add(it)
                PagerSnapShot(
                    pageList = pageList,
                    initialIndex = 0,
                )
            }
    }
}

fun createDummyLazyBookPageSource(
    meta: PageMetaData,
    scope: CoroutineScope,
    settledPageFlow: Flow<AozoraPage?>,
): LazyBookPageSource<AozoraPage> = DummyLazyBookPageSource(
    meta,
    scope,
    settledPageFlow
)


private class DummyLazyBookPageSource(
    private val meta: PageMetaData,
    scope: CoroutineScope,
    settledPageFlow: Flow<AozoraPage?>,
) : LazyBookPageSource<AozoraPage>(scope, settledPageFlow) {
    override fun generatePageFlowBefore(): Flow<AozoraPage> {
        return generatePageFlow(
            aozoraBlockParser = createBlockParser(HtmlLineParser),
            dummyHtml.lineSequence(),
            meta
        )
    }

    override fun generatePageFlowAfter(): Flow<AozoraPage> {
        return flow {
            emit(
                AozoraPage(
                    metaData = meta,
                    lines = listOf(
                        Line(
                            lineHeight = 100f,
                            fontStyle = FontStyle(
                                baseSize = 32f,
                                notationSize = 12f,
                                lineHeightMultiplier = 1.3f,
                                fontType = FontType.DEFAULT,
                            ),
                            elements = listOf(
                                AozoraElement.Text(
                                    text = "Dummy",
                                ),
                            ).toImmutableList()
                        ),
                    ).toImmutableList()
                )
            )
        }

    }
}

private val dummyHtml: String
    get() = """
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi10">序</a></h4></div>
<br />
　<ruby><rb>鬱屈</rb><rp>（</rp><rt>うっくつ</rt><rp>）</rp></ruby>のあまり一日じゅう<ruby><rb>硯</rb><rp>（</rp><rt>すずり</rt><rp>）</rp></ruby>にむかって、心のなかを浮かび過ぎるとりとめもない考えをあれこれと書きつけてみたが、変に気違いじみたものである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi20">一</a></h4></div>
<br />
　何はさて、この世に生まれ出たからには、望ましいこともたくさんあるものである。<br />
　<ruby><rb>帝</rb><rp>（</rp><rt>みかど</rt><rp>）</rp></ruby>の<ruby><rb>御位</rb><rp>（</rp><rt>みくらい</rt><rp>）</rp></ruby>はこのうえなく<ruby><rb>畏</rb><rp>（</rp><rt>おそ</rt><rp>）</rp></ruby>れ多い。皇室の一族の方々は末のほうのお<ruby><rb>方</rb><rp>（</rp><rt>かた</rt><rp>）</rp></ruby>でさえ、人間の種族ではあらせられないのだから尊い。第一の官位を得ている人のおんありさまは申すにおよばない。普通の人でも、<ruby><rb>舎人</rb><rp>（</rp><rt>とねり</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（貴人に仕える下級の官人）</span>を従者に<ruby><rb>賜</rb><rp>（</rp><rt>たま</rt><rp>）</rp></ruby>わるほどの身分になると、たいしたものである。その子や孫ぐらいまでは、落ちぶれてしまっていても活気のあるものである。それ以下の身分になると、分相応に、時運にめぐまれて得意げなのも、当人だけはえらいつもりでいもしようが、つまらぬものである。<br />
　<ruby><rb>法師</rb><rp>（</rp><rt>ほうし</rt><rp>）</rp></ruby>ほど、うらやましくないものはあるまい。「他人には木の<ruby><rb>端</rb><rp>（</rp><rt>はし</rt><rp>）</rp></ruby>か何かのように思われる」と<ruby><rb>清少納言</rb><rp>（</rp><rt>せいしょうなごん</rt><rp>）</rp></ruby>の書いているのも、まことにもっともなことである。世間の評判が高ければ高いほど、えらいもののようには思えなくなる。高僧<ruby><rb>増賀</rb><rp>（</rp><rt>ぞうが</rt><rp>）</rp></ruby>が言ったように、名誉のわずらわしさに仏の<ruby><rb>御教</rb><rp>（</rp><rt>みおし</rt><rp>）</rp></ruby>えにもかなわぬような気がする。しんからの<ruby><rb>世捨人</rb><rp>（</rp><rt>よすてびと</rt><rp>）</rp></ruby>ならば、それはそれで、かくもありたいと思うような人がありもしよう。<br />
　人は<ruby><rb>容貌</rb><rp>（</rp><rt>ようぼう</rt><rp>）</rp></ruby>や<ruby><rb>風采</rb><rp>（</rp><rt>ふうさい</rt><rp>）</rp></ruby>のすぐれたのにだけは、なりたいものである。口をきいたところも聞き苦しからず、<ruby><rb>愛敬</rb><rp>（</rp><rt>あいきょう</rt><rp>）</rp></ruby>があって、おしゃべりでない相手ならばいつでも対座していたい。りっぱな様子の人が、話をしてみると気のきかない<ruby><rb>性根</rb><rp>（</rp><rt>しょうね</rt><rp>）</rp></ruby>があらわれるなどは無念なものである。<br />
　身分や<ruby><rb>風采</rb><rp>（</rp><rt>ふうさい</rt><rp>）</rp></ruby>などは生まれつきのものではあろう。心ならば賢いのを一段と賢くならせることもできないではあるまい。風采や性質のよい人でも、才気がないというのは、品位も落ち、風采のいやな人にさえ無視されるようでは生きがいもない。<br />
　得ておきたいのは真の学問、文学や音楽の<ruby><rb>技倆</rb><rp>（</rp><rt>ぎりょう</rt><rp>）</rp></ruby>。また古い<ruby><rb>典礼</rb><rp>（</rp><rt>てんれい</rt><rp>）</rp></ruby>に明るく、朝廷の儀式や<ruby><rb>作法</rb><rp>（</rp><rt>さほう</rt><rp>）</rp></ruby>について人の手本になれるようならば、たいしたりっぱなものであろう。筆跡なども見苦しからず、すらすらと文を書き、声おもしろく歌の<ruby><rb>拍子</rb><rp>（</rp><rt>ひょうし</rt><rp>）</rp></ruby>を取ることもでき、ことわりたいような様子をしながらも酒も飲めるというようなのが、男としてはいい。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi30">二</a></h4></div>
<br />
　昔の<ruby><rb>聖代</rb><rp>（</rp><rt>せいだい</rt><rp>）</rp></ruby>の政治を念とせず、<ruby><rb>民</rb><rp>（</rp><rt>たみ</rt><rp>）</rp></ruby>の困苦も国の疲労をもかえりみず、すべてに豪華をつくして得意げに、あたりを狭しとふるまっているのを見ると、腹立たしく無思慮なと感ぜられるものである。<br />
「<ruby><rb>衣冠</rb><rp>（</rp><rt>いかん</rt><rp>）</rp></ruby>から馬、車にいたるまでみな、あり合わせのものを用いたがいい、<ruby><rb>華美</rb><rp>（</rp><rt>かび</rt><rp>）</rp></ruby>を求めてはならない」とは、<ruby><rb>藤原師輔</rb><rp>（</rp><rt>ふじわらのもろすけ</rt><rp>）</rp></ruby>公の<ruby><rb>遺誡</rb><rp>（</rp><rt>ゆいかい</rt><rp>）</rp></ruby>にもある。<ruby><rb>順徳院</rb><rp>（</rp><rt>じゅんとくいん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（順徳天皇。在位一二一〇〜二一）</span>が宮中のことをお書きあそばされた<ruby><rb>禁秘抄</rb><rp>（</rp><rt>きんぴしょう</rt><rp>）</rp></ruby>にも「臣下から献上される品は、そまつなのをよいとしなくてはならぬ」とある。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi40">三</a></h4></div>
<br />
　万事に<ruby><rb>傑出</rb><rp>（</rp><rt>けっしゅつ</rt><rp>）</rp></ruby>していても、恋愛の<ruby><rb>趣</rb><rp>（</rp><rt>おもむき</rt><rp>）</rp></ruby>を解しない男は物足りない。玉で作られた<ruby><rb>杯</rb><rp>（</rp><rt>さかずき</rt><rp>）</rp></ruby>に底がないような心もちのするものである。<ruby><rb>露</rb><rp>（</rp><rt>つゆ</rt><rp>）</rp></ruby>や<ruby><rb>霜</rb><rp>（</rp><rt>しも</rt><rp>）</rp></ruby>に<ruby><rb>濡</rb><rp>（</rp><rt>ぬ</rt><rp>）</rp></ruby>れながら、<ruby><rb>当所</rb><rp>（</rp><rt>あてど</rt><rp>）</rp></ruby>もなくうろつき歩いて、親の意見も世間の非難をもはばかっているだけの余裕がないほど、あちらにもこちらにも心定まらず苦しみながら、それでいてひとり寝の時が多く、寝ても熟睡の得られるというときもないというようなのが、おもしろいのである。そうかといって、まるで恋に<ruby><rb>溺</rb><rp>（</rp><rt>おぼ</rt><rp>）</rp></ruby>れきっているというのではなく、女にも<ruby><rb>軽蔑</rb><rp>（</rp><rt>けいべつ</rt><rp>）</rp></ruby>されているというのでないのが、理想的なところである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi50">四</a></h4></div>
<br />
　死後のことをいつも心に忘れずに、仏教の素養などがあるのが奥ゆかしい。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi60">五</a></h4></div>
<br />
　不運にも<ruby><rb>憂</rb><rp>（</rp><rt>うれ</rt><rp>）</rp></ruby>いに沈んでいる人が髪などを<ruby><rb>剃</rb><rp>（</rp><rt>そ</rt><rp>）</rp></ruby>って、世をつまらぬものと思いきったというのよりは、住んでいるのかいないのかと見えるように門を閉じて、世に求めることがあるでもなく日を送っている。<br />
　というほうに自分は賛成する。<br />
　<ruby><rb>顕基中納言</rb><rp>（</rp><rt>あきもとのちゅうなごん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（源顕基）</span>が「罪無くて<ruby><rb>配所</rb><rp>（</rp><rt>はいしょ</rt><rp>）</rp></ruby>の月が見たい」と言った言葉の味も、なるほどと思い当たるであろう。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi70">六</a></h4></div>
<br />
　わが身の<ruby><rb>富貴</rb><rp>（</rp><rt>ふうき</rt><rp>）</rp></ruby>と、<ruby><rb>貧賤</rb><rp>（</rp><rt>ひんせん</rt><rp>）</rp></ruby>とにはかかわらず、子というものはなくてありたい。<br />
　<ruby><rb>前</rb><rp>（</rp><rt>さき</rt><rp>）</rp></ruby>の<ruby><rb>中書王</rb><rp>（</rp><rt>ちゅうしょおう</rt><rp>）</rp></ruby><ruby><rb>兼明</rb><rp>（</rp><rt>かねあきら</rt><rp>）</rp></ruby>親王<span class="sho1" style="font-size: small;">（<ruby><rb>醍醐</rb><rp>（</rp><rt>だいご</rt><rp>）</rp></ruby>天皇の皇子）</span>も、<ruby><rb>九条</rb><rp>（</rp><rt>くじょう</rt><rp>）</rp></ruby>の<ruby><rb>伊通</rb><rp>（</rp><rt>これみち</rt><rp>）</rp></ruby><ruby><rb>太政</rb><rp>（</rp><rt>だじょう</rt><rp>）</rp></ruby>大臣<span class="sho1" style="font-size: small;">（藤原伊通）</span>、<ruby><rb>花園</rb><rp>（</rp><rt>はなぞの</rt><rp>）</rp></ruby>の<ruby><rb>有仁</rb><rp>（</rp><rt>ありひと</rt><rp>）</rp></ruby>左大臣<span class="sho1" style="font-size: small;">（源有仁）</span>など、みな血統のないのを希望された。<ruby><rb>染殿</rb><rp>（</rp><rt>そめどの</rt><rp>）</rp></ruby>の<ruby><rb>良房</rb><rp>（</rp><rt>よしふさ</rt><rp>）</rp></ruby>太政大臣<span class="sho1" style="font-size: small;">（藤原良房）</span>に「子孫のなかったのはよい。<ruby><rb>末裔</rb><rp>（</rp><rt>まつえい</rt><rp>）</rp></ruby>の振るわぬのは困ることである」と<ruby><rb>大鑑</rb><rp>（</rp><rt>おおかがみ</rt><rp>）</rp></ruby>の作者も言っている。聖徳太子が御在世中にお墓をお作らせなされたときも「ここを切り取ってしまえ、あそこも除いたほうがいい。子孫をなくしようと思うからである」と<ruby><rb>仰</rb><rp>（</rp><rt>おお</rt><rp>）</rp></ruby>せられたとやら。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi80">七</a></h4></div>
<br />
　あだし野の<ruby><rb>露</rb><rp>（</rp><rt>つゆ</rt><rp>）</rp></ruby>が消ゆることもなく、<ruby><rb>鳥部山</rb><rp>（</rp><rt>とりべやま</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（現在の京都市東山区嵯峨にあった墓地）</span>に立つ煙が消えもせずに、人の命が常住不断のものであったならば、物のあわれというものもありそうもない。人の世は無常なのがけっこうなのである。<br />
　<ruby><rb>生命</rb><rp>（</rp><rt>いのち</rt><rp>）</rp></ruby>のあるものを見るのに人間ほど長いのはない。かげろうの夕べを待つばかりなのや、夏の<ruby><rb>蝉</rb><rp>（</rp><rt>せみ</rt><rp>）</rp></ruby>の春や秋を知らないのさえもあるのである。よくよく一年を暮らしてみただけでも、このうえもなく、<ruby><rb>悠久</rb><rp>（</rp><rt>ゆうきゅう</rt><rp>）</rp></ruby>である！<br />
　飽かず惜しいと思ったら、千年を過ごしたところで一夜の夢の心地であろう。いつまでも住み果たせられぬ世の中に、見にくい姿になるのを待ち得ても、なんの足しにもなろうか。長生きすれば恥が多いだけのものである。せいぜい四十に足らぬほどで死ぬのがころ合いでもあろうか。<br />
　その時期を過ぎてしまったら、<ruby><rb>容貌</rb><rp>（</rp><rt>ようぼう</rt><rp>）</rp></ruby>を<ruby><rb>愧</rb><rp>（</rp><rt>は</rt><rp>）</rp></ruby>じる心もなく、ただ社会の表面に出しゃばることばかり考え、夕日の落ちてゆくのを見ては子孫のかわいさに<sup class="superscript">（４）</sup>、ますます栄えてゆく日に会おうと生命の欲望を<ruby><rb>逞</rb><rp>（</rp><rt>たくま</rt><rp>）</rp></ruby>しくして、いちずに世情を<ruby><rb>貪</rb><rp>（</rp><rt>むさぼ</rt><rp>）</rp></ruby>る心ばかりが深くなって、美しい感情も忘れがちになってゆきそうなのがあさましい。<br />
<br />
<br />
<div class="jisage_2" style="margin-left: 2em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi90">八</a></h4></div>
"""
