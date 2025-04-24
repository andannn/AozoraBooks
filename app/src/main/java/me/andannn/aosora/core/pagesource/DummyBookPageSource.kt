package me.andannn.aosora.core.pagesource

import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pagesource.page.generatePageSequence
import me.andannn.aosora.core.parser.createBlockParser
import me.andannn.aosora.core.parser.html.HtmlLineParser

fun createDummyBookPageSource(): BookPageSource {
    return object : BookPageSource {
        override fun pageSource(meta: PageMetaData) = sequence {
            generatePageSequence(
                aozoraBlockParser = createBlockParser(HtmlLineParser),
                lineSequence = dummyHtml.lineSequence(),
                meta = meta
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
<br />
　人間の心を<ruby><rb>惑</rb><rp>（</rp><rt>まど</rt><rp>）</rp></ruby>わすものは、<ruby><rb>色情</rb><rp>（</rp><rt>しきじょう</rt><rp>）</rp></ruby>に越すものがない。人間の心というものは、ばかばかしいものだなあ。<ruby><rb>匂</rb><rp>（</rp><rt>にお</rt><rp>）</rp></ruby>いなどは、仮りのものでちょっとのあいだ着物にたき込めてあるものとは承知のうえでも、えも言われぬ匂いなどにはかならず心を鳴りひびかせるものである。<br />
　<ruby><rb>久米</rb><rp>（</rp><rt>くめ</rt><rp>）</rp></ruby>の仙人が、<ruby><rb>洗濯</rb><rp>（</rp><rt>せんたく</rt><rp>）</rp></ruby>していた女の<ruby><rb>脛</rb><rp>（</rp><rt>はぎ</rt><rp>）</rp></ruby>の白いのを見て<ruby><rb>通力</rb><rp>（</rp><rt>つうりき</rt><rp>）</rp></ruby>を失ったというのは<span class="sho1" style="font-size: small;">（『今昔物語集』巻第十一にある）</span>、まことに手足の<ruby><rb>膚</rb><rp>（</rp><rt>はだ</rt><rp>）</rp></ruby>の美しく<ruby><rb>肥</rb><rp>（</rp><rt>こ</rt><rp>）</rp></ruby>え太っていたので、<ruby><rb>外</rb><rp>（</rp><rt>ほか</rt><rp>）</rp></ruby>の色気ではないのだけに、ありそうなことではある。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi100">九</a></h4></div>
<br />
　女は髪の毛のよいのが、格別に、男の目につくものである。人柄や心がけなどは、ものを言っている様子などで物を<ruby><rb>隔</rb><rp>（</rp><rt>へだ</rt><rp>）</rp></ruby>てていてもわかる。ただそこにいるというだけのことで、男の心を<ruby><rb>惑</rb><rp>（</rp><rt>まど</rt><rp>）</rp></ruby>わすこともできるものである。一般に女が心を許す間がらになってからも、満足に眠ることもせず、身の苦労をもいとわず、<ruby><rb>堪</rb><rp>（</rp><rt>た</rt><rp>）</rp></ruby>えられそうにもないことによく<ruby><rb>我慢</rb><rp>（</rp><rt>がまん</rt><rp>）</rp></ruby>しているのは、ただ<ruby><rb>容色</rb><rp>（</rp><rt>ようしょく</rt><rp>）</rp></ruby>愛情を気づかうためである。実に愛着の道は根ざし深く植えられ、その<ruby><rb>源</rb><rp>（</rp><rt>みなもと</rt><rp>）</rp></ruby>の遠く<ruby><rb>錯綜</rb><rp>（</rp><rt>さくそう</rt><rp>）</rp></ruby>したものである。<ruby><rb>色</rb><rp>（</rp><rt>しき</rt><rp>）</rp></ruby>、<ruby><rb>声</rb><rp>（</rp><rt>しょう</rt><rp>）</rp></ruby>、<ruby><rb>香</rb><rp>（</rp><rt>か</rt><rp>）</rp></ruby>、<ruby><rb>味</rb><rp>（</rp><rt>み</rt><rp>）</rp></ruby>、<ruby><rb>触</rb><rp>（</rp><rt>しょく</rt><rp>）</rp></ruby>、<ruby><rb>法</rb><rp>（</rp><rt>ほう</rt><rp>）</rp></ruby>の<ruby><rb>六塵</rb><rp>（</rp><rt>ろくじん</rt><rp>）</rp></ruby>の<ruby><rb>楽欲</rb><rp>（</rp><rt>ぎょうよく</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（欲望）</span>も多い。これらはみな容易に心からたち切ることもできないではないが、ただそのなかの一つ恋愛の執着の押さえ<ruby><rb>難</rb><rp>（</rp><rt>がた</rt><rp>）</rp></ruby>いのは、老人も青年も知者も愚者もみな一ようのように見受けられる。それ<ruby><rb>故</rb><rp>（</rp><rt>ゆえ</rt><rp>）</rp></ruby>、女の髪筋で作った<ruby><rb>綱</rb><rp>（</rp><rt>つな</rt><rp>）</rp></ruby>には<ruby><rb>大象</rb><rp>（</rp><rt>だいぞう</rt><rp>）</rp></ruby>もつながれ、女のはいた<ruby><rb>下駄</rb><rp>（</rp><rt>げた</rt><rp>）</rp></ruby>でこしらえた笛を吹くと、秋山の<ruby><rb>鹿</rb><rp>（</rp><rt>しか</rt><rp>）</rp></ruby>もきっと寄って来ると言い伝えられている。みずから<ruby><rb>戒</rb><rp>（</rp><rt>いまし</rt><rp>）</rp></ruby>めて恐れつつしまなければならないのは、この誘惑である。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi110">一〇</a></h4></div>
<br />
　<ruby><rb>住居</rb><rp>（</rp><rt>すまい</rt><rp>）</rp></ruby>の身分に相応なのは、うき世の仮りの宿りではあるがと思いながらも、楽しいものである。<br />
　身分のある人がゆったりと住んでいるところへは、照らし入る月光までが、いっそうおちついて見えるものである。現代的に華美ではないが、植込みの木々が古色を<ruby><rb>帯</rb><rp>（</rp><rt>お</rt><rp>）</rp></ruby>びて、天然に<ruby><rb>生</rb><rp>（</rp><rt>お</rt><rp>）</rp></ruby>い茂った庭の草も<ruby><rb>趣</rb><rp>（</rp><rt>おもむき</rt><rp>）</rp></ruby>をそえて縁側や<ruby><rb>透垣</rb><rp>（</rp><rt>すいがい</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（竹や木で作った<ruby><rb>隙間</rb><rp>（</rp><rt>すきま</rt><rp>）</rp></ruby>のある垣）</span>の<ruby><rb>配置</rb><rp>（</rp><rt>はいち</rt><rp>）</rp></ruby>もおもしろく、座敷の内のおき道具類も古風なところがあって親しみ多いが奥ゆかしく思われる。多くの<ruby><rb>細工</rb><rp>（</rp><rt>さいく</rt><rp>）</rp></ruby>人がくふうを<ruby><rb>凝</rb><rp>（</rp><rt>こ</rt><rp>）</rp></ruby>らしてりっぱに仕上げた<ruby><rb>唐土</rb><rp>（</rp><rt>もろこし</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（中国）</span>やわが国の珍奇なものを並べ立てておき、庭の植込みにまでも自然のままではなく人工的に作り上げたのは、見た目にも<ruby><rb>窮屈</rb><rp>（</rp><rt>きゅうくつ</rt><rp>）</rp></ruby>に、苦痛を感じさせる。これほどにしたところで、どれほど長いあいだ住んでいられるというものだろうか。また、またたくひまに火になってしまわないともかぎらない。と一見してそんなことも考えさせられる。たいていのことは住居から<ruby><rb>推</rb><rp>（</rp><rt>お</rt><rp>）</rp></ruby>して想像してみることもできる。<ruby><rb>後徳大寺</rb><rp>（</rp><rt>ごとくだいじ</rt><rp>）</rp></ruby>の大臣<ruby><rb>実定卿</rb><rp>（</rp><rt>さねさだきょう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（藤原実定・祖父の<ruby><rb>実能</rb><rp>（</rp><rt>さねよし</rt><rp>）</rp></ruby>から徳大寺家と呼ばれる）</span>が自邸の<ruby><rb>正殿</rb><rp>（</rp><rt>せいでん</rt><rp>）</rp></ruby>の屋根に<ruby><rb>鳶</rb><rp>（</rp><rt>とび</rt><rp>）</rp></ruby>を止まらせまいと<ruby><rb>縄</rb><rp>（</rp><rt>なわ</rt><rp>）</rp></ruby>の張られているのを見た<ruby><rb>西行</rb><rp>（</rp><rt>さいぎょう</rt><rp>）</rp></ruby>が、<ruby><rb>鳶</rb><rp>（</rp><rt>とび</rt><rp>）</rp></ruby>が止まったってなんの悪いこともあるまいに、この<ruby><rb>邸</rb><rp>（</rp><rt>やしき</rt><rp>）</rp></ruby>の<ruby><rb>主</rb><rp>（</rp><rt>あるじ</rt><rp>）</rp></ruby>の大臣が心というのはこれほどのものであったのか。と言って、その後はこの殿には<ruby><rb>伺</rb><rp>（</rp><rt>うかが</rt><rp>）</rp></ruby>わなかったと聞きおよんでいるが、<ruby><rb>綾小路</rb><rp>（</rp><rt>あやのこうじ</rt><rp>）</rp></ruby>の宮<span class="sho1" style="font-size: small;">（<ruby><rb>性恵</rb><rp>（</rp><rt>しょうえ</rt><rp>）</rp></ruby>法親王。亀山天皇の皇子）</span>のお住まいしていらせられる<ruby><rb>小坂殿</rb><rp>（</rp><rt>こさかどの</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（比叡山延暦寺別院の妙法院内の一院）</span>の<ruby><rb>棟</rb><rp>（</rp><rt>むね</rt><rp>）</rp></ruby>に、あるとき縄の引かれていることがあったので、西行の話も思い出されたものであったが、実は<ruby><rb>烏</rb><rp>（</rp><rt>からす</rt><rp>）</rp></ruby>がたくさん来て、池の<ruby><rb>蛙</rb><rp>（</rp><rt>かえる</rt><rp>）</rp></ruby>の<ruby><rb>喰</rb><rp>（</rp><rt>た</rt><rp>）</rp></ruby>べられるのを宮様がかわいそうに<ruby><rb>思召</rb><rp>（</rp><rt>おぼしめ</rt><rp>）</rp></ruby>されたからであると人が話したので、これはまたけっこうなと感ぜられたことであった。徳大寺にも、なにか事情があったかもしれない。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi120">一一</a></h4></div>
<br />
　十月のころ、<ruby><rb>栗栖野</rb><rp>（</rp><rt>くるすの</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（現在の京都市<ruby><rb>山科</rb><rp>（</rp><rt>やましな</rt><rp>）</rp></ruby>の一部）</span>という所を過ぎてある山里へたずね入ったことがあったが、奥深い<ruby><rb>苔</rb><rp>（</rp><rt>こけ</rt><rp>）</rp></ruby>の細道を踏みわけて行ってみると、心細い<ruby><rb>有様</rb><rp>（</rp><rt>ありさま</rt><rp>）</rp></ruby>に住んでいる小家があった。木の葉に埋もれた<ruby><rb>筧</rb><rp>（</rp><rt>かけひ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（泉などから水を引く<ruby><rb>樋</rb><rp>（</rp><rt>とい</rt><rp>）</rp></ruby>）</span>の<ruby><rb>滴</rb><rp>（</rp><rt>したたり</rt><rp>）</rp></ruby>ぐらいよりほかは訪れる人とてもなかろう。<ruby><rb>閼伽棚</rb><rp>（</rp><rt>あかだな</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（仏前に供える水の器を置く棚）</span>に<ruby><rb>菊</rb><rp>（</rp><rt>きく</rt><rp>）</rp></ruby><ruby><rb>紅葉</rb><rp>（</rp><rt>もみじ</rt><rp>）</rp></ruby>などを折り散らしているのは、これでも住んでる人があるからであろう。こんなふうにしてでも生活できるものであると、感心していると、向こうの庭のほうに大きな<ruby><rb>蜜柑</rb><rp>（</rp><rt>みかん</rt><rp>）</rp></ruby>の木の、枝もたわむばかりに実のなっているのがあって、それに厳重に<ruby><rb>柵</rb><rp>（</rp><rt>さく</rt><rp>）</rp></ruby>をめぐらしてあるのであった。すこし<ruby><rb>興</rb><rp>（</rp><rt>きょう</rt><rp>）</rp></ruby>がさめて、こんな木がなければよかったのになあと思った。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi130">一二</a></h4></div>
<br />
　同じ心を持った人としんみり話をして、おもしろいことや、世のなかの無常なことなどを<ruby><rb>隔</rb><rp>（</rp><rt>へだ</rt><rp>）</rp></ruby>てなく語り<ruby><rb>慰</rb><rp>（</rp><rt>なぐさ</rt><rp>）</rp></ruby>め合ってこそうれしいわけであるが、同じ心の人などがあるはずもないから、すこしも意見の相違がないように対話をしていたならば、ひとりでいるような退屈な心もちがあるであろう。<br />
　双方言いたいだけをなるほどと思って聞いてこそ、かいもあるものであるから、すこしばかりは違ったところのある人であってこそ、自分はそう思われないと反対をしたり、こういうわけだからこうだなどと述べ合ったりしたなら、退屈も<ruby><rb>紛</rb><rp>（</rp><rt>まぎ</rt><rp>）</rp></ruby>れそうに思うのに、事実としてはすこしく意見の相違した人とは、つまらぬ雑談でもしているあいだはともかく、本気に心の友としてみるとたいへん考え方がくい違っているところが出てくるのは、なさけないことである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi140">一三</a></h4></div>
<br />
　ひとり<ruby><rb>灯下</rb><rp>（</rp><rt>とうか</rt><rp>）</rp></ruby>に書物をひろげて見も知らぬ時代の人を友とするのが、このうえもない楽しいことではある。書ならば<ruby><rb>文選</rb><rp>（</rp><rt>もんぜん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（昭明太子撰。周から梁時代の詩文をまとめたもの）</span>などの心に訴えるところの多い巻々、<ruby><rb>白氏文集</rb><rp>（</rp><rt>はくしもんじゅう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（唐の詩人・白楽天の詩文集）</span>、<ruby><rb>老子</rb><rp>（</rp><rt>ろうし</rt><rp>）</rp></ruby>の言説、<ruby><rb>荘子</rb><rp>（</rp><rt>そうし</rt><rp>）</rp></ruby>の<ruby><rb>南華真経</rb><rp>（</rp><rt>なんかしんぎょう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（『荘子』のこと）</span>だとか、わが国の学者たちの著書も、古い時代のものには心にふれることどもが多い。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi150">一四</a></h4></div>
<br />
　和歌となると一だんと興味の深いものである、<ruby><rb>下賤</rb><rp>（</rp><rt>げせん</rt><rp>）</rp></ruby>な<ruby><rb>樵夫</rb><rp>（</rp><rt>きこり</rt><rp>）</rp></ruby>の仕事も、歌に<ruby><rb>詠</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>んでみると趣味があるし、恐ろしい<ruby><rb>猪</rb><rp>（</rp><rt>いのしし</rt><rp>）</rp></ruby>なども、<ruby><rb>臥猪</rb><rp>（</rp><rt>ふすい</rt><rp>）</rp></ruby>の<ruby><rb>床</rb><rp>（</rp><rt>とこ</rt><rp>）</rp></ruby>などと言うと優美に感じられる。ちかごろの歌は気のきいたところがあると思われるのはあるが、古い時代の歌のように、なにとなく言外に、心に訴え心に<ruby><rb>魅惑</rb><rp>（</rp><rt>みわく</rt><rp>）</rp></ruby>を感じさせるのはない。<ruby><rb>貫之</rb><rp>（</rp><rt>つらゆき</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（紀貫之）</span>が「糸による物ならなくに<sup class="superscript">（５）</sup>」と<ruby><rb>詠</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>んだ歌は、古今集の中でも<ruby><rb>歌屑</rb><rp>（</rp><rt>うたくず</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（つまらない歌）</span>だとか言い伝えられているが、現代の人に詠める作風とは思えない。その時の歌には<ruby><rb>風情</rb><rp>（</rp><rt>ふぜい</rt><rp>）</rp></ruby>も句法もこんな種類のものが多い。この歌に限って、こう<ruby><rb>貶</rb><rp>（</rp><rt>おと</rt><rp>）</rp></ruby>しめられているのも<ruby><rb>合点</rb><rp>（</rp><rt>がてん</rt><rp>）</rp></ruby>がゆかぬ。源氏物語には「ものとはなしに<sup class="superscript">（６）</sup>」と書いてはいる。新古今では、「残る松さへ峰にさびしき<sup class="superscript">（７）</sup>」という歌をさして歌屑にしているのは、なるほどいくぶん雑なところがあるかもしれない。けれどもこの歌だって合評のときにはよろしいという評決があって、あとで<ruby><rb>後鳥羽院</rb><rp>（</rp><rt>ごとばいん</rt><rp>）</rp></ruby>からもわざわざ感心したとの<ruby><rb>仰</rb><rp>（</rp><rt>おお</rt><rp>）</rp></ruby>せがあったと<ruby><rb>家長</rb><rp>（</rp><rt>いえなが</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（源家長）</span>の日記に書いてある。<br />
　歌の道だけは昔と変わってはいないなどというが、はたしてどうか。今も歌に詠み合っている同じ<ruby><rb>詞</rb><rp>（</rp><rt>ことば</rt><rp>）</rp></ruby>なり、名勝地でも、古人の詠んだのは全然同じものではない。わかりやすく、すらすらと、姿も上品で、実感も多い。<ruby><rb>梁塵秘抄</rb><rp>（</rp><rt>りょうじんひしょう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（後白河上皇の撰になる歌謡集）</span>の<ruby><rb>謡</rb><rp>（</rp><rt>うた</rt><rp>）</rp></ruby>い<ruby><rb>物</rb><rp>（</rp><rt>もの</rt><rp>）</rp></ruby>の歌詞は、また格別に実感に富んでいるように思う。昔の人は、出まかせのような言葉のはしまでもどうしてこうも、みなりっぱに聞こえるものであろうか。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi160">一五</a></h4></div>
<br />
　どこにもせよ、しばらく旅行に出るということは目の<ruby><rb>覚</rb><rp>（</rp><rt>さ</rt><rp>）</rp></ruby>めるような心もちのするものである。その地方をあちらこちらと見物してまわり、<ruby><rb>田舎臭</rb><rp>（</rp><rt>いなかくさ</rt><rp>）</rp></ruby>いところ、山里などは、はなはだ珍しいことが多い。都の<ruby><rb>留守宅</rb><rp>（</rp><rt>るすたく</rt><rp>）</rp></ruby>へ<ruby><rb>伝手</rb><rp>（</rp><rt>つて</rt><rp>）</rp></ruby>を求めて手紙を送るにしても、あれとこれとをいい、ついでを心がけておけなどと言ってやるのも、楽しい。こんな場合などにあって何かとよく気のつくものである。手回りの品なども良い品はいっそう良く感ぜられ、働きのある人物はふだんよりはいっそう引き立って見える。寺や社などに知らぬ顔をしてお<ruby><rb>籠</rb><rp>（</rp><rt>こも</rt><rp>）</rp></ruby>りをしているなどもおもしろいものである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi170">一六</a></h4></div>
<br />
　<ruby><rb>神楽</rb><rp>（</rp><rt>かぐら</rt><rp>）</rp></ruby>というものは活気もあり、趣味の多いものである。一般の音楽では、笛、ひちりき<span class="sho1" style="font-size: small;">（竹の笛の一種）</span>が<ruby><rb>好</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>い。常に聞きたいと思うものは<ruby><rb>琵琶</rb><rp>（</rp><rt>びわ</rt><rp>）</rp></ruby>と<ruby><rb>和琴</rb><rp>（</rp><rt>わごん</rt><rp>）</rp></ruby>とである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi180">一七</a></h4></div>
<br />
　山寺に<ruby><rb>引</rb><rp>（</rp><rt>ひ</rt><rp>）</rp></ruby>き<ruby><rb>籠</rb><rp>（</rp><rt>こも</rt><rp>）</rp></ruby>っていて仏に<ruby><rb>仕</rb><rp>（</rp><rt>つか</rt><rp>）</rp></ruby>えているのこそ、退屈もせず、心の<ruby><rb>濁</rb><rp>（</rp><rt>にご</rt><rp>）</rp></ruby>りも洗い清められる気のするものである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi190">一八</a></h4></div>
<br />
　人はわが身の節度をよく守って、<ruby><rb>驕</rb><rp>（</rp><rt>おご</rt><rp>）</rp></ruby>りを打ち払い、<ruby><rb>財</rb><rp>（</rp><rt>ざい</rt><rp>）</rp></ruby>を持たず、世間に<ruby><rb>執着</rb><rp>（</rp><rt>しゅうちゃく</rt><rp>）</rp></ruby>しないのがりっぱである。昔から賢い人で富んでいたという例は、はなはだ少ない。<br />
　中国の<ruby><rb>許由</rb><rp>（</rp><rt>きょゆう</rt><rp>）</rp></ruby>という人は身に着けたたくわえは何一つなく、水をさえ手で飲んでいたのを見たので、人が<ruby><rb>瓢箪</rb><rp>（</rp><rt>ひょうたん</rt><rp>）</rp></ruby>を与えたところ、あるとき木の枝にかけておいたのが風に吹かれて音を立てるので騒々しいと言って捨てた。ふたたび手で<ruby><rb>掬</rb><rp>（</rp><rt>すく</rt><rp>）</rp></ruby>い上げて水を飲んだ。どんなにか心の中がさっぱりしていたものであったろうか。また<ruby><rb>孫晨</rb><rp>（</rp><rt>そんしん</rt><rp>）</rp></ruby>という人は冬、<ruby><rb>夜着</rb><rp>（</rp><rt>やぎ</rt><rp>）</rp></ruby>がなくて<ruby><rb>藁</rb><rp>（</rp><rt>わら</rt><rp>）</rp></ruby>が一<ruby><rb>束</rb><rp>（</rp><rt>たば</rt><rp>）</rp></ruby>あったのを、夜になるとそのなかにもぐりこみ、朝になると丸めてしまっておいた。中国の人はこれをりっぱなことに思ったればこそ、書き記して後世に伝えたのであろう。こんな人があっても日本でなら話にも伝えられまい。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi200">一九</a></h4></div>
<br />
　季節の移り変わりこそ、何かにつけて<ruby><rb>興</rb><rp>（</rp><rt>きょう</rt><rp>）</rp></ruby>の深いものではある。<br />
　感情を動かすのは秋が第一であるとはだれしも言うけれども、それはそれでいいとして、もういっそう心に活気の出るものは、春の<ruby><rb>景色</rb><rp>（</rp><rt>けしき</rt><rp>）</rp></ruby>でもあろう。鳥の声などは、とくに早く春の感情をあらわし、のどかな日ざしに、<ruby><rb>垣根</rb><rp>（</rp><rt>かきね</rt><rp>）</rp></ruby>の草が<ruby><rb>萌</rb><rp>（</rp><rt>も</rt><rp>）</rp></ruby>えはじめる時分から、いくぶんと春の<ruby><rb>趣</rb><rp>（</rp><rt>おもむき</rt><rp>）</rp></ruby>ふかく<ruby><rb>霞</rb><rp>（</rp><rt>かすみ</rt><rp>）</rp></ruby>も立ちなびいて、花もおいおいと目につきやすくなるころになるというのに、おりから西風がつづいて心おちつく<ruby><rb>間</rb><rp>（</rp><rt>ま</rt><rp>）</rp></ruby>もなく花は散ってしまう。青葉のころになるまでなにかにつけて心をなやますことが多い。花たちばなはいまさらでもなく知られているが、梅の<ruby><rb>匂</rb><rp>（</rp><rt>にお</rt><rp>）</rp></ruby>いにはひとしお過ぎ去ったことどもが思いかえされて恋しい思いがする。<ruby><rb>山吹</rb><rp>（</rp><rt>やまぶき</rt><rp>）</rp></ruby>の<ruby><rb>清楚</rb><rp>（</rp><rt>せいそ</rt><rp>）</rp></ruby>なのや<ruby><rb>藤</rb><rp>（</rp><rt>ふじ</rt><rp>）</rp></ruby>の心細い<ruby><rb>有様</rb><rp>（</rp><rt>ありさま</rt><rp>）</rp></ruby>をしたのなど、すべて春には注意せずにいられないような事象が多い。<br />
　<ruby><rb>仏生会</rb><rp>（</rp><rt>ぶっしょうえ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（<ruby><rb>釈迦</rb><rp>（</rp><rt>しゃか</rt><rp>）</rp></ruby>の誕生日の行事。陰暦四月八日）</span>のころ、<ruby><rb>加茂</rb><rp>（</rp><rt>かも</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（京都・賀茂神社のこと）</span>のお祭のころ、若葉の<ruby><rb>梢</rb><rp>（</rp><rt>こずえ</rt><rp>）</rp></ruby>がすずしげに茂ってゆく時分こそ、人の世のあわれが身にしみて、人の恋しさも増すものであると<ruby><rb>仰</rb><rp>（</rp><rt>おお</rt><rp>）</rp></ruby>せられた方があったが、まったくそのとおりである。五月あやめの<ruby><rb>節句</rb><rp>（</rp><rt>せっく</rt><rp>）</rp></ruby>のころ、田植の時節に<ruby><rb>水鶏</rb><rp>（</rp><rt>くいな</rt><rp>）</rp></ruby>の戸をたたくように鳴くのも心細くないことがあろうか。六月になって、<ruby><rb>賤</rb><rp>（</rp><rt>いや</rt><rp>）</rp></ruby>しい小家に夕顔の白く見えて<ruby><rb>蚊遣火</rb><rp>（</rp><rt>かやりび</rt><rp>）</rp></ruby>のくすぶっているのも趣がある。六月の<ruby><rb>大祓</rb><rp>（</rp><rt>おおはらい</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（六月と十二月に宮中や神社で行なわれる神事）</span>もまたよい。<ruby><rb>七夕</rb><rp>（</rp><rt>たなばた</rt><rp>）</rp></ruby>を祭るのはにぎやかに優美である。おいおい<ruby><rb>夜寒</rb><rp>（</rp><rt>よさむ</rt><rp>）</rp></ruby>になってきて<ruby><rb>雁</rb><rp>（</rp><rt>かり</rt><rp>）</rp></ruby>が鳴き渡るころ、<ruby><rb>萩</rb><rp>（</rp><rt>はぎ</rt><rp>）</rp></ruby>の<ruby><rb>下葉</rb><rp>（</rp><rt>したば</rt><rp>）</rp></ruby>が赤味を<ruby><rb>帯</rb><rp>（</rp><rt>お</rt><rp>）</rp></ruby>びる時分、<ruby><rb>早稲田</rb><rp>（</rp><rt>わせだ</rt><rp>）</rp></ruby>を<ruby><rb>刈</rb><rp>（</rp><rt>か</rt><rp>）</rp></ruby>り<ruby><rb>乾</rb><rp>（</rp><rt>ほ</rt><rp>）</rp></ruby>すなど、さまざまな興味は秋に限って多い。<ruby><rb>野分</rb><rp>（</rp><rt>のわき</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（秋から冬の暴風）</span>の朝というものが趣の多いものである。言いつづけてくると、すべて、源氏物語や枕草子などで<ruby><rb>陳腐</rb><rp>（</rp><rt>ちんぷ</rt><rp>）</rp></ruby>になってはいるけれど、同じことだから言い出さないという気にもならない。思うところは言ってしまわないと気もちが悪いから、筆にまかせた。つまらぬ遊びごとで<ruby><rb>破</rb><rp>（</rp><rt>やぶ</rt><rp>）</rp></ruby>き捨てるつもりのものだから、人が見るはずもあるまい。<br />
　さて冬枯れの景色というものは、秋にくらべてたいして劣るまいと思われる。<ruby><rb>水際</rb><rp>（</rp><rt>みずぎわ</rt><rp>）</rp></ruby>の草には<ruby><rb>紅葉</rb><rp>（</rp><rt>もみじ</rt><rp>）</rp></ruby>が散りとまって、<ruby><rb>霜</rb><rp>（</rp><rt>しも</rt><rp>）</rp></ruby>のまっ白においている朝、庭にひいた流れから煙のような気が立ちのぼっているのなどは、わけておもしろい。<br />
　年の暮れの押し迫って、だれも彼もみな忙しがっているころがまた、このうえなく人の心を引くものである。すさまじいものと決めてしまって見る人もない月が寒く澄みきっている二十日過ぎの空こそ、心細いものではある。おん<ruby><rb>仏名会</rb><rp>（</rp><rt>ぶつみょうえ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（十二月十九〜二十一日の宮中の仏事）</span>だの<ruby><rb>荷前</rb><rp>（</rp><rt>のさき</rt><rp>）</rp></ruby>の<ruby><rb>使</rb><rp>（</rp><rt>つかい</rt><rp>）</rp></ruby>が立つなど、趣味深く尊いものである。こんなお儀式がいくつも、春を迎える忙しさのなかにかさねがさね取り行なわれる様子が、すばらしい。<br />
　<ruby><rb>追儺</rb><rp>（</rp><rt>ついな</rt><rp>）</rp></ruby>から<ruby><rb>四方拝</rb><rp>（</rp><rt>しほうはい</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（それぞれ、十二月末日と元日の宮中の行事）</span>につづいてゆくのがおもしろい。つごもりの夜はたいそう暗いのを、<ruby><rb>松明</rb><rp>（</rp><rt>たいまつ</rt><rp>）</rp></ruby>などともして人の家をたずねて歩き回り、なんだか知らないがぎょうぎょうしくわめき立て、足も地につかぬかとばかり急ぐが、夜明け方になると、さすがに、物音がなくなって世間がひっそりする。一年の<ruby><rb>名残</rb><rp>（</rp><rt>なご</rt><rp>）</rp></ruby>りかと心ぼそくもある。死人の来る夜というので魂を祭る風習はこのごろでは都ではしなくなったのに、関東ではまだしていたのは、奥ゆかしかった。こんなふうに一夜が明けてゆく空の景色は昨日と変わっているところもないのに、なんだか新鮮に貴重な感じがする。<ruby><rb>大路</rb><rp>（</rp><rt>おおじ</rt><rp>）</rp></ruby>の有様は松飾りをして行き交う人もはなやかに飾り、うれしげに見えるのがまたおもしろい。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi210">二〇</a></h4></div>
<br />
　<ruby><rb>何某</rb><rp>（</rp><rt>なにがし</rt><rp>）</rp></ruby>とやらいった<ruby><rb>世捨人</rb><rp>（</rp><rt>よすてびと</rt><rp>）</rp></ruby>が、この世の足手まといも持たない自分にとっては、ただ空の見納めがこころ残りであると言ったのは、なるほどそう感じられたであろう。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi220">二一</a></h4></div>
<br />
　すべてのことは、月を見るにつけて<ruby><rb>慰</rb><rp>（</rp><rt>なぐさ</rt><rp>）</rp></ruby>められるものである。ある人が月ほどおもしろいものはあるまいと言ったところが、別の一人が<ruby><rb>露</rb><rp>（</rp><rt>つゆ</rt><rp>）</rp></ruby>こそ<ruby><rb>風情</rb><rp>（</rp><rt>ふぜい</rt><rp>）</rp></ruby>が多いと抗議を出したのは愉快である。<ruby><rb>折</rb><rp>（</rp><rt>おり</rt><rp>）</rp></ruby>にかないさえすれば、なんだって<ruby><rb>趣</rb><rp>（</rp><rt>おもむき</rt><rp>）</rp></ruby>のないものはあるまい。<br />
　月花は無論のこと、風というものが、あれで人の心もちをひくものである。岩にくだけて清く流れる水のありさまこそ、季節にかかわらずよいものである。「<ruby><rb><img src="../../../gaiji/1-86/1-86-54.png" alt="※(「さんずい＋元」、第3水準1-86-54)" class="gaiji" /></rb><rp>（</rp><rt>げん</rt><rp>）</rp></ruby><ruby><rb>湘</rb><rp>（</rp><rt>しょう</rt><rp>）</rp></ruby><ruby><rb>日夜</rb><rp>（</rp><rt>にちや</rt><rp>）</rp></ruby>東に流れ去る。<ruby><rb>愁人</rb><rp>（</rp><rt>しゅうじん</rt><rp>）</rp></ruby>のためにとどまることしばらくもせず」という詩<span class="sho1" style="font-size: small;">（唐の詩人・<ruby><rb>戴叔倫</rb><rp>（</rp><rt>たいしゅくりん</rt><rp>）</rp></ruby>の作。<img src="../../../gaiji/1-86/1-86-54.png" alt="※(「さんずい＋元」、第3水準1-86-54)" class="gaiji" />・湘はともに杭州の川）</span>を見たことがあったが、なかなか心にひびいた。また<ruby><rb><img src="../../../gaiji/1-47/1-47-83.png" alt="※(「（禾＋尤）／山」、第3水準1-47-83)" class="gaiji" />康</rb><rp>（</rp><rt>けいこう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（三国・魏の人で、竹林七賢の一人）</span>も「<ruby><rb>山沢</rb><rp>（</rp><rt>さんたく</rt><rp>）</rp></ruby>にあそびて<ruby><rb>魚鳥</rb><rp>（</rp><rt>ぎょちょう</rt><rp>）</rp></ruby>を見れば心<ruby><rb>慰</rb><rp>（</rp><rt>たの</rt><rp>）</rp></ruby>しむ」と言っている。人を遠ざかって水草の美しいあたりを<ruby><rb>逍遥</rb><rp>（</rp><rt>しょうよう</rt><rp>）</rp></ruby>するほど、心の慰められるものはあるまい。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi230">二二</a></h4></div>
<br />
　何事につけても、昔がとかく<ruby><rb>慕</rb><rp>（</rp><rt>した</rt><rp>）</rp></ruby>わしい。現代ふうは、このうえなく下品になってしまったようだ。<ruby><rb>指物</rb><rp>（</rp><rt>さしもの</rt><rp>）</rp></ruby>師の作った<ruby><rb>細工物</rb><rp>（</rp><rt>さいくもの</rt><rp>）</rp></ruby>類にしても、昔の様式が趣味深く思われる。手紙の文句なども昔の<ruby><rb>反古</rb><rp>（</rp><rt>ほご</rt><rp>）</rp></ruby>がりっぱである。口でいうだけの言葉にしたところが、昔は「車もたげよ」「火かかげよ」と言ったものを、現代の人は「もてあげよ」「かきあげよ」などと言う。<ruby><rb>主殿寮</rb><rp>（</rp><rt>とのもりょう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（宮中の役所の一つ）</span>の「<ruby><rb>人数</rb><rp>（</rp><rt>にんじゅ</rt><rp>）</rp></ruby>立て」と言うべきを、「たちあかししろくせよ」<span class="sho1" style="font-size: small;">（<ruby><rb>松明</rb><rp>（</rp><rt>たいまつ</rt><rp>）</rp></ruby>を明るくせよ）</span>と言い、<ruby><rb>最勝講</rb><rp>（</rp><rt>さいしょうこう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（五月に宮中で行なわれる仏事）</span>の<ruby><rb>御聴聞所</rb><rp>（</rp><rt>みちょうもんじょ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（前記の仏事の際、天皇が高僧の講義を聞かれる御座所）</span>は「<ruby><rb>御講</rb><rp>（</rp><rt>ごこう</rt><rp>）</rp></ruby>の<ruby><rb>盧</rb><rp>（</rp><rt>ろ</rt><rp>）</rp></ruby>」というべきを「<ruby><rb>講盧</rb><rp>（</rp><rt>こうろ</rt><rp>）</rp></ruby>」などと言っている。心外なことであると、さる老人が申された。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi240">二三</a></h4></div>
<br />
　<ruby><rb>衰</rb><rp>（</rp><rt>おとろ</rt><rp>）</rp></ruby>えた末の世ではあるが、それでも雲の上の<ruby><rb>神々</rb><rp>（</rp><rt>こうごう</rt><rp>）</rp></ruby>しい御様子は世俗を離れて尊貴を感じるのである。<br />
　<ruby><rb>露台</rb><rp>（</rp><rt>ろだい</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（宮中にある板張りの一角）</span>、<ruby><rb>朝餉</rb><rp>（</rp><rt>あさがれい</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（清涼殿内の一間で、天皇が略式の食事をとる所）</span>、<ruby><rb>何殿</rb><rp>（</rp><rt>なにでん</rt><rp>）</rp></ruby>、<ruby><rb>何門</rb><rp>（</rp><rt>なにもん</rt><rp>）</rp></ruby>などはりっぱにも聞こえるであろう。<ruby><rb>下々</rb><rp>（</rp><rt>しもじも</rt><rp>）</rp></ruby>にもある<ruby><rb>小蔀</rb><rp>（</rp><rt>こじとみ</rt><rp>）</rp></ruby>、<ruby><rb>小板敷</rb><rp>（</rp><rt>こいたじき</rt><rp>）</rp></ruby>、<ruby><rb>高遣戸</rb><rp>（</rp><rt>たかやりど</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（それぞれ、清涼殿の窓、板敷き、戸）</span>などでさえ高雅に思われるではないか。<br />
「<ruby><rb>陣</rb><rp>（</rp><rt>じん</rt><rp>）</rp></ruby>に<ruby><rb>夜</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>のもうけせよ<sup class="superscript">（９）</sup>」というのは、どっしりしている。<ruby><rb>夜御殿</rb><rp>（</rp><rt>よるのおとど</rt><rp>）</rp></ruby>をば「かいともし、とうよ<sup class="superscript">（<span dir="ltr">10</span>）</sup>」などというのもまた、ありがたい。<ruby><rb>上卿</rb><rp>（</rp><rt>しょうけい</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（儀式の首座）</span>の陣で事務を<ruby><rb>執</rb><rp>（</rp><rt>と</rt><rp>）</rp></ruby>っておられる様は申すにおよばぬこと、下役の者どもが、得意ぶった様子で事務に熟達しているのも興味がある。すこぶる寒いころの徹夜にあちらこちらで居眠りをしている者を見かけるのがおかしい。「<ruby><rb>内侍所</rb><rp>（</rp><rt>ないしどころ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（神鏡を奉ずる<ruby><rb>温明</rb><rp>（</rp><rt>うんめい</rt><rp>）</rp></ruby>殿のことで、<ruby><rb>内侍司</rb><rp>（</rp><rt>ないしのつかさ</rt><rp>）</rp></ruby>という女官がつとめる）</span>の<ruby><rb>御鈴</rb><rp>（</rp><rt>みすず</rt><rp>）</rp></ruby>の音<span class="sho1" style="font-size: small;">（彼女らが天皇の参拝のときにふる）</span>はめでたく優雅なものです」などと、徳大寺殿の<ruby><rb>基実</rb><rp>（</rp><rt>もとざね</rt><rp>）</rp></ruby><ruby><rb>太政</rb><rp>（</rp><rt>だじょう</rt><rp>）</rp></ruby>大臣<span class="sho1" style="font-size: small;">（藤原<ruby><rb>公孝</rb><rp>（</rp><rt>きんたか</rt><rp>）</rp></ruby>）</span>が申しておられる。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi250">二四</a></h4></div>
<br />
　<ruby><rb>斎宮</rb><rp>（</rp><rt>いつきのみや</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（天皇即位の際、伊勢神宮に奉仕する内親王・皇女）</span>が<ruby><rb>野宮</rb><rp>（</rp><rt>ののみや</rt><rp>）</rp></ruby>におらせられるおん<ruby><rb>有様</rb><rp>（</rp><rt>ありさま</rt><rp>）</rp></ruby><sup class="superscript">（<span dir="ltr">11</span>）</sup>こそ、しごく優美に<ruby><rb>興趣</rb><rp>（</rp><rt>きょうしゅ</rt><rp>）</rp></ruby>のあるものに感ぜられるではないか。経、仏などは<ruby><rb>忌</rb><rp>（</rp><rt>い</rt><rp>）</rp></ruby>んで、「<ruby><rb>染</rb><rp>（</rp><rt>そ</rt><rp>）</rp></ruby>め<ruby><rb>紙</rb><rp>（</rp><rt>がみ</rt><rp>）</rp></ruby>」「<ruby><rb>中子</rb><rp>（</rp><rt>なかご</rt><rp>）</rp></ruby>」などと言うのもおもしろい。元来が、神社というものはなんとなく取り得のある奥ゆかしいものだ。年を<ruby><rb>経</rb><rp>（</rp><rt>へ</rt><rp>）</rp></ruby>た森の<ruby><rb>景色</rb><rp>（</rp><rt>けしき</rt><rp>）</rp></ruby>が超世間だのに、<ruby><rb>玉垣</rb><rp>（</rp><rt>たまがき</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（神社の垣根）</span>をめぐり渡して<ruby><rb>榊</rb><rp>（</rp><rt>さかき</rt><rp>）</rp></ruby>に<ruby><rb>木綿</rb><rp>（</rp><rt>ゆう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（コウゾの皮の繊維で作った布で、<ruby><rb>幣帛</rb><rp>（</rp><rt>へいはく</rt><rp>）</rp></ruby>として榊にかけて献ずる）</span>をかけてあるところなど堂々たらぬはずはない。わけてもすぐれているのは伊勢、<ruby><rb>加茂</rb><rp>（</rp><rt>かも</rt><rp>）</rp></ruby>、<ruby><rb>春日</rb><rp>（</rp><rt>かすが</rt><rp>）</rp></ruby>、平野、<ruby><rb>住吉</rb><rp>（</rp><rt>すみよし</rt><rp>）</rp></ruby>、<ruby><rb>三輪</rb><rp>（</rp><rt>みわ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（伊勢神宮、京都・賀茂神社、奈良・春日神社、京都・平野神社、大阪・住吉神社、奈良・<ruby><rb>大神</rb><rp>（</rp><rt>おおみわ</rt><rp>）</rp></ruby>神社）</span>、<ruby><rb>貴船</rb><rp>（</rp><rt>きぶね</rt><rp>）</rp></ruby>、吉田、<ruby><rb>大原野</rb><rp>（</rp><rt>おおはらの</rt><rp>）</rp></ruby>、松の尾、梅の宮<span class="sho1" style="font-size: small;">（以上は京都の神社）</span>である。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi260">二五</a></h4></div>
<br />
　<ruby><rb>飛鳥川</rb><rp>（</rp><rt>あすかがわ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（奈良県明日香村あたりを流れる）</span>の<ruby><rb>淵瀬</rb><rp>（</rp><rt>ふちせ</rt><rp>）</rp></ruby>のように、変わりやすいのが無常のこの世のならいであるから、時移り、事は過ぎて、歓楽や<ruby><rb>哀傷</rb><rp>（</rp><rt>あいしょう</rt><rp>）</rp></ruby>の<ruby><rb>往来</rb><rp>（</rp><rt>ゆきき</rt><rp>）</rp></ruby>して、<ruby><rb>華麗</rb><rp>（</rp><rt>かれい</rt><rp>）</rp></ruby>であった場所も住む人のない野原となり、変わらぬ家があれば、住む人のほうで変わってしまった。たとい昔ながらに咲き誇るとも<ruby><rb>桃李</rb><rp>（</rp><rt>とうり</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（モモとスモモ）</span>は物言わぬものであるから、だれを相手に昔語りをしようか。まして見も知らぬ遠い昔の高貴な人々の<ruby><rb>趾</rb><rp>（</rp><rt>あと</rt><rp>）</rp></ruby>にいたっては、実にはかない。<br />
　たとえば<ruby><rb>藤原道長</rb><rp>（</rp><rt>ふじわらのみちなが</rt><rp>）</rp></ruby>の<ruby><rb>京極殿</rb><rp>（</rp><rt>きょうごくでん</rt><rp>）</rp></ruby>や<ruby><rb>法成寺</rb><rp>（</rp><rt>ほうじょうじ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（道長の邸宅と、その東、鴨川近くに建立した寺）</span>などを見ると、昔の<ruby><rb>志</rb><rp>（</rp><rt>こころざし</rt><rp>）</rp></ruby>だけは残って時勢が一変しているのに注意を促されて、胸の迫る思いがある。<ruby><rb>御堂殿</rb><rp>（</rp><rt>みどうどの</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（道長のこと）</span>が善美をつくして造営せられて、<ruby><rb>荘園</rb><rp>（</rp><rt>しょうえん</rt><rp>）</rp></ruby>を多く寄付され、自分の一族を皇室の<ruby><rb>藩屏</rb><rp>（</rp><rt>はんぺい</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（垣根、転じて天子の守護）</span>、国家の<ruby><rb>柱石</rb><rp>（</rp><rt>ちゅうせき</rt><rp>）</rp></ruby>として、後世まで変わるまいと信じておられたその当時には、どんな時勢になってこんなふうに荒廃するものと思ってみられようはずもない。<ruby><rb>大門</rb><rp>（</rp><rt>だいもん</rt><rp>）</rp></ruby>、<ruby><rb>金堂</rb><rp>（</rp><rt>こんどう</rt><rp>）</rp></ruby>などは近いころまではまだあったが、<ruby><rb>正和</rb><rp>（</rp><rt>しょうわ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（一三一二—一七）</span>のころに<ruby><rb>南門</rb><rp>（</rp><rt>なんもん</rt><rp>）</rp></ruby>は焼けた。金堂はその後、横倒れになってしまったままで、それをもう建て直そうとする<ruby><rb>企</rb><rp>（</rp><rt>くわだ</rt><rp>）</rp></ruby>てすらない。<ruby><rb>無量寿院</rb><rp>（</rp><rt>むりょうじゅいん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（阿弥陀堂）</span>ばかりがその形見となって残っている。一丈六尺<span class="sho1" style="font-size: small;">（約五メートル弱。一般に化身仏の高さをいう）</span>の仏体が九つ、権威を見せて並んでおられる。<ruby><rb>行成大納言</rb><rp>（</rp><rt>こうぜいのだいなごん</rt><rp>）</rp></ruby>が名筆の<ruby><rb>額</rb><rp>（</rp><rt>がく</rt><rp>）</rp></ruby>や、<ruby><rb>兼行</rb><rp>（</rp><rt>かねゆき</rt><rp>）</rp></ruby>の筆の扉<span class="sho1" style="font-size: small;">（藤原行成、源兼行ともに能筆として名高い）</span>が鮮明に見えているのは興趣が多い。<ruby><rb>法華堂</rb><rp>（</rp><rt>ほっけどう</rt><rp>）</rp></ruby>もまだ残っているであろう。それとてもいつまで残っていようか。これほどの<ruby><rb>残骸</rb><rp>（</rp><rt>ざんがい</rt><rp>）</rp></ruby>さえとどめていない場所は、自然、<ruby><rb>礎</rb><rp>（</rp><rt>いしずえ</rt><rp>）</rp></ruby>の石だけが残るということにもなるが、由来を判然と知る人もなかろう。それゆえ何かにつけて見ることもできないのちの世のことまで思慮を<ruby><rb>尽</rb><rp>（</rp><rt>つ</rt><rp>）</rp></ruby>くしておくというのも、たのみにはならない。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi270">二六</a></h4></div>
<br />
　風に吹かれるまでもなく変わりうつろうのが人の心であるから、<ruby><rb>親睦</rb><rp>（</rp><rt>しんぼく</rt><rp>）</rp></ruby>した当時を思い出してみると身に<ruby><rb>沁</rb><rp>（</rp><rt>し</rt><rp>）</rp></ruby>みて聞いた一言一句も忘れもせぬのに、自分の生活にかかわりもない人のようになってしまう恋の一般性を考えると、死別にもまさる悲しみである。それゆえ、白い糸が染められるのを見て悲しみ、道の<ruby><rb>小路</rb><rp>（</rp><rt>こうじ</rt><rp>）</rp></ruby>が分かれるのを<ruby><rb>嘆</rb><rp>（</rp><rt>なげ</rt><rp>）</rp></ruby>く人もあったのではあろう。<ruby><rb>堀川院</rb><rp>（</rp><rt>ほりかわいん</rt><rp>）</rp></ruby>百首<span class="sho1" style="font-size: small;">（堀河天皇の下で、十六人の廷臣が百首ずつ計千六百の歌を<ruby><rb>詠</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>んだもの）</span>の歌の中にある——<br />
<div class="jisage_2" style="margin-left: 2em">
昔見し<ruby><rb>妹</rb><rp>（</rp><rt>いも</rt><rp>）</rp></ruby>がかきねは荒れにけり<br />
　　つばなまじりの<ruby><rb>菫</rb><rp>（</rp><rt>すみれ</rt><rp>）</rp></ruby>のみして<sup class="superscript">（<span dir="ltr">13</span>）</sup><br />
</div>
　<ruby><rb>哀</rb><rp>（</rp><rt>あわ</rt><rp>）</rp></ruby>れを誘う<ruby><rb>風情</rb><rp>（</rp><rt>ふぜい</rt><rp>）</rp></ruby>は、実感から出たものであったろう。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi280">二七</a></h4></div>
<br />
　<ruby><rb>御譲位</rb><rp>（</rp><rt>ごじょうい</rt><rp>）</rp></ruby>の御儀式がすんで、三種の<ruby><rb>神器</rb><rp>（</rp><rt>じんぎ</rt><rp>）</rp></ruby>を新帝にお渡しあそばされるときは、ひどく心細く感ぜられるものである。<ruby><rb>花園上皇</rb><rp>（</rp><rt>はなぞのじょうこう</rt><rp>）</rp></ruby>が<ruby><rb>高御座</rb><rp>（</rp><rt>たかみくら</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（天皇の位のこと）</span>をお<ruby><rb>譲</rb><rp>（</rp><rt>ゆず</rt><rp>）</rp></ruby>りあそばされたつぎの春、お<ruby><rb>詠</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>みあそばされたとやらうけたまわる——<br />
<div class="jisage_2" style="margin-left: 2em">
<ruby><rb>殿守</rb><rp>（</rp><rt>とのもり</rt><rp>）</rp></ruby>のとものみやつこよそにして<br />
　　<ruby><rb>掃</rb><rp>（</rp><rt>はら</rt><rp>）</rp></ruby>わぬ庭に花ぞ散りしく<sup class="superscript">（<span dir="ltr">14</span>）</sup><br />
</div>
　新帝の<ruby><rb>御代</rb><rp>（</rp><rt>みよ</rt><rp>）</rp></ruby>の務めの忙しいのにかまけて、上皇の御所には参る者もないというのはまことに<ruby><rb>寂</rb><rp>（</rp><rt>さび</rt><rp>）</rp></ruby>しいことではある。こういう場合に人の心の真実は現われもしよう。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi290">二八</a></h4></div>
<br />
　<ruby><rb>諒闇</rb><rp>（</rp><rt>りょうあん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（天皇が父母の喪に服すこと）</span>の年ほど悲しいことはない。<ruby><rb>倚盧</rb><rp>（</rp><rt>いろ</rt><rp>）</rp></ruby>の御所<span class="sho1" style="font-size: small;">（諒闇の初めに<ruby><rb>籠</rb><rp>（</rp><rt>こも</rt><rp>）</rp></ruby>るところ）</span>の<ruby><rb>有様</rb><rp>（</rp><rt>ありさま</rt><rp>）</rp></ruby>にしたところが、<ruby><rb>板敷</rb><rp>（</rp><rt>いたじき</rt><rp>）</rp></ruby>を下げて<ruby><rb>葦</rb><rp>（</rp><rt>あし</rt><rp>）</rp></ruby>で編んだ<ruby><rb>御簾</rb><rp>（</rp><rt>みす</rt><rp>）</rp></ruby>をかけ、布の<ruby><rb>帽額</rb><rp>（</rp><rt>もこう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（御簾の上に引く幕状のもの）</span>は<ruby><rb>粗野</rb><rp>（</rp><rt>そや</rt><rp>）</rp></ruby>に、お道具類も粗略になり、百官の<ruby><rb>装束</rb><rp>（</rp><rt>しょうぞく</rt><rp>）</rp></ruby>や、<ruby><rb>太刀</rb><rp>（</rp><rt>たち</rt><rp>）</rp></ruby>、<ruby><rb>平緒</rb><rp>（</rp><rt>ひらお</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（太刀の飾りひも）</span>までが平素と異なっているのは、ただごとではない思いをさせる。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi300">二九</a></h4></div>
<br />
　静かに思うと、何かにつけて過去のことどもばかり恋しくなってきてしかたがない。人の寝静まってのち、<ruby><rb>夜長</rb><rp>（</rp><rt>よなが</rt><rp>）</rp></ruby>の退屈しのぎにごたごたした道具など片づけ、死後には残しておきたくないような<ruby><rb>古反古</rb><rp>（</rp><rt>ふるほご</rt><rp>）</rp></ruby>などを破り捨てているうちに、<ruby><rb>亡</rb><rp>（</rp><rt>な</rt><rp>）</rp></ruby>くなった人の手習いや絵など<ruby><rb>慰</rb><rp>（</rp><rt>なぐさ</rt><rp>）</rp></ruby>みにかき散らしたものを見つけ出すと、ただもうその当時の心もちになってしまう。いま現に生きている人のものだって、いつどんな折のものであったろうかと考えてみるのは、身にしみる味である。使い古した道具なども、気にもとめず久しいあいだ用いなれているのは、感に<ruby><rb>堪</rb><rp>（</rp><rt>た</rt><rp>）</rp></ruby>えぬものである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi310">三〇</a></h4></div>
<br />
　人の<ruby><rb>亡</rb><rp>（</rp><rt>な</rt><rp>）</rp></ruby>くなったあとほど悲しいものはない。<ruby><rb>中陰</rb><rp>（</rp><rt>ちゅういん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（死後の四十九日間）</span>のあいだ山里などに引っ越していて狭い不便な所へ多人数が寄り集まり、のちの法事などを営んでいるのは気ぜわしい。日数の<ruby><rb>経</rb><rp>（</rp><rt>た</rt><rp>）</rp></ruby>つことの早さはくらべものもない。最後の日にはたいへん情けないふうになっておたがいに口をきくこともなく<ruby><rb>各々</rb><rp>（</rp><rt>おのおの</rt><rp>）</rp></ruby>われがちに<ruby><rb>荷纏</rb><rp>（</rp><rt>にまと</rt><rp>）</rp></ruby>めして、ちりぢりに別れていってしまう。もとの<ruby><rb>住居</rb><rp>（</rp><rt>すまい</rt><rp>）</rp></ruby>へ帰って来てからがまた一段と悲しいことが多いのである。しかじかのことは、<ruby><rb>慎</rb><rp>（</rp><rt>つつ</rt><rp>）</rp></ruby>しむべし、あとに生き残っている人のために<ruby><rb>忌</rb><rp>（</rp><rt>い</rt><rp>）</rp></ruby>むべき事柄であるなどと言うが、この悲しみの最中にそんなことぐらいでもよさそうなものを、人間の心というものはやはりいやなものであると感じさせられる。年月が経ってもすこしも忘れられぬということではないが、去る者は日々に<ruby><rb>疎</rb><rp>（</rp><rt>うと</rt><rp>）</rp></ruby>しというとおり、忘れられないといううちにも、その当時とは違ってくるものか、雑談に笑い<ruby><rb>興</rb><rp>（</rp><rt>きょう</rt><rp>）</rp></ruby>じたりする。<ruby><rb>遺骸</rb><rp>（</rp><rt>いがい</rt><rp>）</rp></ruby>は人里遠い山の中へ葬って、<ruby><rb>忌日</rb><rp>（</rp><rt>きじつ</rt><rp>）</rp></ruby>などにだけ<ruby><rb>参詣</rb><rp>（</rp><rt>さんけい</rt><rp>）</rp></ruby>してみると、ほどなく<ruby><rb>卒都婆</rb><rp>（</rp><rt>そとば</rt><rp>）</rp></ruby>に<ruby><rb>苔</rb><rp>（</rp><rt>こけ</rt><rp>）</rp></ruby>が生えて、木の葉に埋められ、夕方に吹く風や<ruby><rb>夜半</rb><rp>（</rp><rt>よわ</rt><rp>）</rp></ruby>の月などばかりがわずかに<ruby><rb>慰</rb><rp>（</rp><rt>なぐさ</rt><rp>）</rp></ruby>めてくれるのである。それも思い出してたずねて来る人が生きているうちはまだしもいいが、それらも早晩はみな亡くなってしまって、話に聞き伝えるだけにすぎぬ人などは、なんで悲しいなど思おうや。かくてあとを<ruby><rb>弔</rb><rp>（</rp><rt>とむら</rt><rp>）</rp></ruby>うことも打ち絶えてしまうと、どこの人であったやら名さえ知れなくなり<sup class="superscript">（<span dir="ltr">15</span>）</sup>、年々の春の草ばかりは、心ある人に感動を与えもしよう。<br />
　ついには、<ruby><rb>嵐</rb><rp>（</rp><rt>あらし</rt><rp>）</rp></ruby>に<ruby><rb>咽</rb><rp>（</rp><rt>むせ</rt><rp>）</rp></ruby>んでいた松も千年とは経たぬうちに<ruby><rb>薪</rb><rp>（</rp><rt>たきぎ</rt><rp>）</rp></ruby>に<ruby><rb>摧</rb><rp>（</rp><rt>くだ</rt><rp>）</rp></ruby>かれ<sup class="superscript">（<span dir="ltr">16</span>）</sup>、<ruby><rb>古墳</rb><rp>（</rp><rt>こふん</rt><rp>）</rp></ruby>は<ruby><rb>犁</rb><rp>（</rp><rt>す</rt><rp>）</rp></ruby>かれて田となる。そのあとかたさえなくなるのが悲しい。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi320">三一</a></h4></div>
<br />
　雪のおもしろく降った朝、ある人のところへ用があって手紙をやるに、雪のことには一言もふれなかったところが、その返事に、「この雪をなんと見るかと一筆申されぬほどのひねくれた<ruby><rb>野暮</rb><rp>（</rp><rt>やぼ</rt><rp>）</rp></ruby>な人のいうことなんか聞いてあげられましょうか、どこまでもなさけないお心ですね」とあったのは、<ruby><rb>興</rb><rp>（</rp><rt>きょう</rt><rp>）</rp></ruby>があった。今はもう<ruby><rb>亡</rb><rp>（</rp><rt>な</rt><rp>）</rp></ruby>き人のことだから、こればかりのことも忘れがたい。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi330">三二</a></h4></div>
<br />
　九月二十日時分のこと、ある方のお誘いのお供をして、夜の明けるまで、月を見歩いたことがあったが、お思い出しになった家があるというので、案内を受けておはいりになられた。庭の荒れている<ruby><rb>露</rb><rp>（</rp><rt>つゆ</rt><rp>）</rp></ruby>の多いところに、とくにというのではなくふだんから<ruby><rb>焚</rb><rp>（</rp><rt>た</rt><rp>）</rp></ruby>いているらしい<ruby><rb>薫香</rb><rp>（</rp><rt>たきもの</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（いくつかの香を練り合わせた<ruby><rb>練香</rb><rp>（</rp><rt>ねりこう</rt><rp>）</rp></ruby>）</span>がしっとりと<ruby><rb>匂</rb><rp>（</rp><rt>にお</rt><rp>）</rp></ruby>うている。世を忍んでただならぬ方の住んでいるらしい様子が、まことに風雅である。自分のいっしょに行った方はいいかげんおられて出てこられたが、自分はことの優美に感心して、ものかげからしばらく見ていたら、家のなかの人は<ruby><rb>妻戸</rb><rp>（</rp><rt>つまど</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（両開きの板戸）</span>をすこしおしあけて月を見る様子であった。客を送り出してすぐ奥に引っこんでしまったとしたら、うちこわしであったろう。まだ見ている人がいようなどとは知るはずがあるものではない。これらのことはただ日常の心がけによってなされたものであろう。彼女はその後まもなく死んだと聞いた。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi340">三三</a></h4></div>
<br />
　今の<ruby><rb>内裏</rb><rp>（</rp><rt>だいり</rt><rp>）</rp></ruby>が落成して、<ruby><rb>有職</rb><rp>（</rp><rt>ゆうそく</rt><rp>）</rp></ruby>の人々<span class="sho1" style="font-size: small;">（朝廷の儀式・作法などにくわしい人たち）</span>に見せられたところが、どこにも欠点がないというので、もうお引き移りの日も迫っていたのに、<ruby><rb>玄輝門院</rb><rp>（</rp><rt>げんきもんいん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（後深草天皇の妃）</span>がご覧あそばされて、<ruby><rb>閑院殿</rb><rp>（</rp><rt>かんいんどの</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（平安京内に臨時に設けられた里内裏と呼ばれた皇居のこと）</span>の<ruby><rb>櫛形</rb><rp>（</rp><rt>くしがた</rt><rp>）</rp></ruby>の窓は、<ruby><rb>円</rb><rp>（</rp><rt>まる</rt><rp>）</rp></ruby>っこく<ruby><rb>縁</rb><rp>（</rp><rt>ふち</rt><rp>）</rp></ruby>もありはしなかったと<ruby><rb>仰</rb><rp>（</rp><rt>おお</rt><rp>）</rp></ruby>せられた。まことにえらいものであった。これは壁にきざみを入れて木で<ruby><rb>縁</rb><rp>（</rp><rt>ふち</rt><rp>）</rp></ruby>をしていたもので、違っていたから改められた。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi350">三四</a></h4></div>
<br />
　<ruby><rb>甲香</rb><rp>（</rp><rt>かいこう</rt><rp>）</rp></ruby>は、<ruby><rb>螺</rb><rp>（</rp><rt>ほらがい</rt><rp>）</rp></ruby>のようなものが、形が小さく、口のところが細長く出ている貝の<ruby><rb>蓋</rb><rp>（</rp><rt>ふた</rt><rp>）</rp></ruby>である。<ruby><rb>武蔵</rb><rp>（</rp><rt>むさし</rt><rp>）</rp></ruby>の金沢<span class="sho1" style="font-size: small;">（現在の横浜市金沢）</span>という浦で取れたのを、土地の人は「へなだり」と呼んでいるということであった。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi360">三五</a></h4></div>
<br />
　字のへたな人が、平気で手紙を書き散らすのは<ruby><rb>好</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>い。見苦しいからと代筆をさせているのはいやみなものである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi370">三六</a></h4></div>
<br />
　長いあいだ訪れもせぬが<ruby><rb>恨</rb><rp>（</rp><rt>うら</rt><rp>）</rp></ruby>んでいるであろう。自分のぶしょうのせいと申しわけもない気もちがしていると、女のほうから「手のすいた召使いをひとりよこしてください」などと言ってくるのは、ありがたくうれしい。「そんな気風のがいい」とある人が語った。同感のことである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi380">三七</a></h4></div>
<br />
　いつもわけ<ruby><rb>隔</rb><rp>（</rp><rt>へだ</rt><rp>）</rp></ruby>てなく慣れ親しんでいる人が、何かの<ruby><rb>拍子</rb><rp>（</rp><rt>ひょうし</rt><rp>）</rp></ruby>に、わけ<ruby><rb>隔</rb><rp>（</rp><rt>へだ</rt><rp>）</rp></ruby>てがましく様子ぶっている<ruby><rb>有様</rb><rp>（</rp><rt>ありさま</rt><rp>）</rp></ruby>をしているのは、いまさらそんなことをするでもあるまいという人もあるかもしれないが、やはりきちんとした<ruby><rb>好</rb><rp>（</rp><rt>よ</rt><rp>）</rp></ruby>い人だなあと感じられるものである。平素あまり親密でもない人が打ち解けたことを話し出したりするのも、それから好きになったりするものである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi390">三八</a></h4></div>
<br />
　<ruby><rb>名聞利益</rb><rp>（</rp><rt>みょうもんりやく</rt><rp>）</rp></ruby>のために心を支配されて、おちついた時もなく一生を苦しみ通すのはばかげたことである。財産が多くなると一身の<ruby><rb>護</rb><rp>（</rp><rt>まも</rt><rp>）</rp></ruby>りのためには不充分なものである。危害を求め、<ruby><rb>煩悶</rb><rp>（</rp><rt>はんもん</rt><rp>）</rp></ruby>を招く<ruby><rb>媒</rb><rp>（</rp><rt>なかだち</rt><rp>）</rp></ruby>になる。<ruby><rb>白氏文集</rb><rp>（</rp><rt>はくしもんじゅう</rt><rp>）</rp></ruby>にあるように、<ruby><rb>黄金</rb><rp>（</rp><rt>こがね</rt><rp>）</rp></ruby>を積み上げて<ruby><rb>北斗</rb><rp>（</rp><rt>ほくと</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（北斗星）</span>を支えるほどの身分になってみても、他人に迷惑をかけるだけのことである。俗人の目を喜ばせる<ruby><rb>慰</rb><rp>（</rp><rt>たの</rt><rp>）</rp></ruby>しみというのもつまらぬ。大きな車や、<ruby><rb>肥</rb><rp>（</rp><rt>こ</rt><rp>）</rp></ruby>えた馬、黄金や<ruby><rb>珠玉</rb><rp>（</rp><rt>たま</rt><rp>）</rp></ruby>も、心ある人にはいやなばかげたものと思われるであろう。金は山に捨て、玉は<ruby><rb>淵</rb><rp>（</rp><rt>ふち</rt><rp>）</rp></ruby>へ投げるがいい。古人が言うように利欲に<ruby><rb>惑</rb><rp>（</rp><rt>まど</rt><rp>）</rp></ruby>うのは最も愚かな人である。<br />
　<ruby><rb>不朽</rb><rp>（</rp><rt>ふきゅう</rt><rp>）</rp></ruby>の名を世に残すことは望ましい。位が高く身分が尊いからといって、必ずしもすぐれた人とは言えまい。<ruby><rb>愚者迂人</rb><rp>（</rp><rt>ぐしゃうじん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（おろかでにぶい人）</span>でも貴い家に生まれ、時にあえば、高い位にも上り<ruby><rb>驕奢</rb><rp>（</rp><rt>きょうしゃ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（ぜいたく）</span>をきわめるものである。りっぱな聖人であった人でも、自分から辞退して低い位にいたり時代にあわないでしまった人も多かった。いちずに高位高官を希望するものも利欲に<ruby><rb>惑</rb><rp>（</rp><rt>まど</rt><rp>）</rp></ruby>うにつづいて第二のばかである。知恵と精神とにおいてこそ世に<ruby><rb>勝</rb><rp>（</rp><rt>すぐ</rt><rp>）</rp></ruby>れた名誉をも残したいものであるが、熟考してみると名誉を愛するというのはつまりは人の評判を喜ぶわけである。<ruby><rb>褒</rb><rp>（</rp><rt>ほ</rt><rp>）</rp></ruby>める人も、<ruby><rb>毀</rb><rp>（</rp><rt>そし</rt><rp>）</rp></ruby>る人も、いつまでもこの世にとどまっているわけではない。伝え聞く人々だとて、またさっさとこの世を去ってしまう。だれに対して恥じ、だれに知られようと願おうか。<ruby><rb>誉</rb><rp>（</rp><rt>ほま</rt><rp>）</rp></ruby>れは同時に<ruby><rb>毀</rb><rp>（</rp><rt>そし</rt><rp>）</rp></ruby>りの根本である。死後の名が伝わったとていっこう無益ではないか。これを願うのも第三の愚かである。<br />
　しかし、しいて知恵を求め、賢くなりたいと思う人のために言ってみるとすれば、なまなかの知恵が出るので<ruby><rb>虚偽</rb><rp>（</rp><rt>きょぎ</rt><rp>）</rp></ruby>が生じた。才能というのも<ruby><rb>煩悩</rb><rp>（</rp><rt>ぼんのう</rt><rp>）</rp></ruby>の増長したものである。聞き伝えたり、習って覚え知ったのは、ほんとうの知恵ではない。どんなのを知恵といったものだろうか。可も不可も一本のものである。どんなものを善といったものだろうか。<ruby><rb>真人</rb><rp>（</rp><rt>しんじん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（まことの道を知り、完全な道徳を身につけた人）</span>は知もなく、徳もなく、<ruby><rb>功名</rb><rp>（</rp><rt>こうみょう</rt><rp>）</rp></ruby>もなく、名誉もない。だれがこれを理解し、これを世に伝えようや。べつに徳を隠し、愚を守るというわけでもない。本来が<ruby><rb>賢愚得失</rb><rp>（</rp><rt>けんぐとくしつ</rt><rp>）</rp></ruby>の境地には住んでいないのだからである。迷いの心をいだいて名聞利得を求めるのはこのとおりである。すべてみな、まちがいである。言うに足らず。願うにも足りない。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi400">三九</a></h4></div>
<br />
　ある人が、<ruby><rb>法然上人</rb><rp>（</rp><rt>ほうねんしょうにん</rt><rp>）</rp></ruby>に、「念仏の時に眠くなってしまって<ruby><rb>行</rb><rp>（</rp><rt>ぎょう</rt><rp>）</rp></ruby>ができませんが、どうしてこの障害を防いだらよろしゅうございましょうか」と言うと、「目が<ruby><rb>覚</rb><rp>（</rp><rt>さ</rt><rp>）</rp></ruby>めたら念仏をなさい」と答えられた。じつに尊かった。<br />
　また、<ruby><rb>往生</rb><rp>（</rp><rt>おうじょう</rt><rp>）</rp></ruby>は確実なものと思えば確実、不確かと思えば不確かであるとも<ruby><rb>仰</rb><rp>（</rp><rt>おお</rt><rp>）</rp></ruby>せられた。これも尊い。また疑いながらでも、念仏をすれば<ruby><rb>往生</rb><rp>（</rp><rt>おうじょう</rt><rp>）</rp></ruby>するとも仰せられた。これもまた尊い。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi410">四〇</a></h4></div>
<br />
　<ruby><rb>因幡</rb><rp>（</rp><rt>いなば</rt><rp>）</rp></ruby>の国<span class="sho1" style="font-size: small;">（現在の鳥取県）</span>に何の<ruby><rb>入道</rb><rp>（</rp><rt>にゅうどう</rt><rp>）</rp></ruby>とかいう者の娘が<ruby><rb>美貌</rb><rp>（</rp><rt>びぼう</rt><rp>）</rp></ruby>だというので、多くの人が結婚を申しこんだが、この娘はただ<ruby><rb>栗</rb><rp>（</rp><rt>くり</rt><rp>）</rp></ruby>ばかり食べて、米の<ruby><rb>類</rb><rp>（</rp><rt>たぐい</rt><rp>）</rp></ruby>はいっこう食べなかったので、こんな変人は人の嫁にはやれないといって、親が許可しなかった。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi420">四一</a></h4></div>
<br />
　五月五日、<ruby><rb>加茂</rb><rp>（</rp><rt>かも</rt><rp>）</rp></ruby>の<ruby><rb>競馬</rb><rp>（</rp><rt>くらべうま</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（<ruby><rb>上賀茂</rb><rp>（</rp><rt>かみがも</rt><rp>）</rp></ruby>神社で催される）</span>を見物に行ったが、車の前に、<ruby><rb>雑人</rb><rp>（</rp><rt>ぞうにん</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（身分の低い者）</span>どもが多数立ちはだかって見えなかったから、<ruby><rb>一行</rb><rp>（</rp><rt>いっこう</rt><rp>）</rp></ruby>はそれぞれ車を<ruby><rb>下</rb><rp>（</rp><rt>お</rt><rp>）</rp></ruby>りて<ruby><rb>埒</rb><rp>（</rp><rt>らち</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（馬場の垣）</span>のそばへすり寄ったけれど、特別に人が混雑していて割りこまれそうにもなかった。<br />
　こんなおりから<ruby><rb>樗</rb><rp>（</rp><rt>おうち</rt><rp>）</rp></ruby>の木に<ruby><rb>坊主</rb><rp>（</rp><rt>ぼうず</rt><rp>）</rp></ruby>が登って、木の<ruby><rb>股</rb><rp>（</rp><rt>また</rt><rp>）</rp></ruby>のところで見物していた。木に取っつかまっていて、よく眠っていて落ちそうになると目をさますことがたびたびであった。これを見ている人が<ruby><rb>嘲笑</rb><rp>（</rp><rt>ちょうしょう</rt><rp>）</rp></ruby>して「実にばかな<ruby><rb>奴</rb><rp>（</rp><rt>やつ</rt><rp>）</rp></ruby>だなあ、あんな危ない枝の上で、平気で居眠りしているのだから」と言っていたので、その時心に思いついたままを「われらが死の到来が今の今であるかもしれない。それを忘れて、物を見て暮らしている。このばかさかげんは、あの坊主以上でしょうに」と言ったので、前にいた人々も「ほんとうに、そうですね、最もばかでしたね」と言って、みな後をふり返って見て「こちらへおはいりなさい」と場所を立ち<ruby><rb>退</rb><rp>（</rp><rt>の</rt><rp>）</rp></ruby>いて呼び入れた。<br />
　このくらいの道理を、だれだって気がつかないはずはなかろうに、こういう場合、思いがけない気がして思い当たったのでもあろうか、人は<ruby><rb>木石</rb><rp>（</rp><rt>ぼくせき</rt><rp>）</rp></ruby>ではないから時と場合によっては、ものに感ずることもあるのだ。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi430">四二</a></h4></div>
<br />
　<ruby><rb>唐橋中将</rb><rp>（</rp><rt>からはしのちゅうじょう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（源<ruby><rb>雅清</rb><rp>（</rp><rt>まさきよ</rt><rp>）</rp></ruby>）</span>という人の子息に、<ruby><rb>行雅僧都</rb><rp>（</rp><rt>ぎょうがそうず</rt><rp>）</rp></ruby>といって密教の教理の先生をしている僧があった。のぼせる病気があって年とってくるにしたがって、鼻がつまり、息もしにくくなったのでいろいろ治療もしたけれど、重態になって、目や<ruby><rb>眉</rb><rp>（</rp><rt>まゆ</rt><rp>）</rp></ruby>や<ruby><rb>額</rb><rp>（</rp><rt>ひたい</rt><rp>）</rp></ruby>など<ruby><rb>腫</rb><rp>（</rp><rt>は</rt><rp>）</rp></ruby>れぼったく<ruby><rb>覆</rb><rp>（</rp><rt>おお</rt><rp>）</rp></ruby>いかぶさってきたので、物も見えず、二の舞の<ruby><rb>面</rb><rp>（</rp><rt>めん</rt><rp>）</rp></ruby>のように色赤く、恐ろしげな面相に似て、ただ恐ろしげな、鬼の顔になり、目はいただきにつき、額のあたりが鼻になったりしたので、のちには同じ寺中の人にも会わず、引き<ruby><rb>籠</rb><rp>（</rp><rt>こも</rt><rp>）</rp></ruby>り、長いあいだ病んだあげく、死んだ。妙な病気もあったものである。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi440">四三</a></h4></div>
<br />
　晩春のころ、のどかに美しい空に品位のある住宅の奥深く、植込みの木々も年を<ruby><rb>経</rb><rp>（</rp><rt>へ</rt><rp>）</rp></ruby>た庭に散り<ruby><rb>萎</rb><rp>（</rp><rt>しお</rt><rp>）</rp></ruby>れている花の素通りしてしまうのが惜しいようなのを、はいって行ってのぞいて見ると、南向きのほうの<ruby><rb>格子</rb><rp>（</rp><rt>こうし</rt><rp>）</rp></ruby>はみな閉めきってさびしそうであるが、東のほうに向かっては<ruby><rb>妻戸</rb><rp>（</rp><rt>つまど</rt><rp>）</rp></ruby>をいいかげんにあけているのを、<ruby><rb>御簾</rb><rp>（</rp><rt>みす</rt><rp>）</rp></ruby>の破れ目から見ると、<ruby><rb>風采</rb><rp>（</rp><rt>ふうさい</rt><rp>）</rp></ruby>のさっぱりした男が、年のころ二十ばかりで、改まったではないが、奥ゆかしく、のんびりした様子で机の上に本をひろげて見ているのであった。いったいどんな<ruby><rb>素姓</rb><rp>（</rp><rt>すじょう</rt><rp>）</rp></ruby>の人やら知りたいような気がした。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi450">四四</a></h4></div>
<br />
　そまつな竹の<ruby><rb>編戸</rb><rp>（</rp><rt>あみど</rt><rp>）</rp></ruby>の中から、ごく若い男が、月光のなかでは色合いははっきりしないが、<ruby><rb>光沢</rb><rp>（</rp><rt>こうたく</rt><rp>）</rp></ruby>のある<ruby><rb>狩衣</rb><rp>（</rp><rt>かりぎぬ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（貴族の日常着）</span>に、濃い<ruby><rb>紫</rb><rp>（</rp><rt>むらさき</rt><rp>）</rp></ruby>色の<ruby><rb>指貫</rb><rp>（</rp><rt>さしぬき</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（はかまの一種）</span>を着け、<ruby><rb>由緒</rb><rp>（</rp><rt>ゆいしょ</rt><rp>）</rp></ruby>ありげな様子をしているが、ちいさな童子をひとり<ruby><rb>供</rb><rp>（</rp><rt>とも</rt><rp>）</rp></ruby>に連れて遠い田の中の細い道を稲葉の露に<ruby><rb>濡</rb><rp>（</rp><rt>ぬ</rt><rp>）</rp></ruby>れながら歩いていくとき、笛をなんともいえぬ音に吹きなぐさんでいた。聞いておもしろいと感ずるほどの人もあるまいにと思われる場所柄だから、笛の主のゆくえが知りたくて見送りながら行くと、笛は吹きやめて山の<ruby><rb>麓</rb><rp>（</rp><rt>ふもと</rt><rp>）</rp></ruby>に表門のある中にはいった。<ruby><rb>榻</rb><rp>（</rp><rt>しじ</rt><rp>）</rp></ruby>に<ruby><rb>轅</rb><rp>（</rp><rt>ながえ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（牛をつなぐ牛車の棒。とめておくときにそれを置く台が榻）</span>をもたせかけた車の見えるのも市中よりは目につくような気がしたので、<ruby><rb>下部</rb><rp>（</rp><rt>しもべ</rt><rp>）</rp></ruby>の男に聞いてみると「これこれの宮様がおいでになっていられるので、御法事でもあそばすのでしょうか」と言う。<br />
　<ruby><rb>御堂</rb><rp>（</rp><rt>みどう</rt><rp>）</rp></ruby>のほうには<ruby><rb>法師</rb><rp>（</rp><rt>ほうし</rt><rp>）</rp></ruby>たちが来ていた。<ruby><rb>夜寒</rb><rp>（</rp><rt>よさむ</rt><rp>）</rp></ruby>の風に誘われてくる<ruby><rb>空薫</rb><rp>（</rp><rt>そらだき</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（それとわからないように香をたきくゆらすこと）</span>の<ruby><rb>匂</rb><rp>（</rp><rt>にお</rt><rp>）</rp></ruby>いも身にしみるようである。正殿から御堂への<ruby><rb>廊</rb><rp>（</rp><rt>ろう</rt><rp>）</rp></ruby>をかよう女房の追い風の用意なども、人目のない山里とも思われず行きとどいていた。<br />
　思う存分に茂った秋の野は、置きどころのないほどしとどな露に埋まって虫の音がものを訴えるように、庭前の流水の音が静かである。市中の空よりも、雲の往来も速いように感ぜられ、月の晴れたり曇ったりするのも<ruby><rb>頻繁</rb><rp>（</rp><rt>ひんぱん</rt><rp>）</rp></ruby>であった。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi460">四五</a></h4></div>
<br />
　<ruby><rb>従</rb><rp>（</rp><rt>じゅ</rt><rp>）</rp></ruby>二位<ruby><rb>藤原公世</rb><rp>（</rp><rt>ふじわらのきんよ</rt><rp>）</rp></ruby>の兄の、<ruby><rb>良覚僧正</rb><rp>（</rp><rt>りょうがくそうじょう</rt><rp>）</rp></ruby>と申された方は、とても怒りっぽい人であった。寺のそばに大きな<ruby><rb>榎</rb><rp>（</rp><rt>えのき</rt><rp>）</rp></ruby>の木があったので、人々が榎の僧正と呼んだ。こんな名は<ruby><rb>怪</rb><rp>（</rp><rt>け</rt><rp>）</rp></ruby>しからぬというので、その木は<ruby><rb>伐</rb><rp>（</rp><rt>き</rt><rp>）</rp></ruby>ってしまった。その根があったので<ruby><rb>切杭</rb><rp>（</rp><rt>きりくい</rt><rp>）</rp></ruby>の僧正と呼んだ。僧正はますます立腹して切杭を掘りかえして捨てたので、その跡が大きな堀になってあったから、<ruby><rb>堀池</rb><rp>（</rp><rt>ほりけ</rt><rp>）</rp></ruby>の僧正とつけた。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi470">四六</a></h4></div>
<br />
　<ruby><rb>柳原</rb><rp>（</rp><rt>やなぎはら</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（京都市上京区柳原町のあたり）</span>の付近に、<ruby><rb>強盗法印</rb><rp>（</rp><rt>ごうとうほういん</rt><rp>）</rp></ruby>と名づけられた僧があった。たびたび強盗にあったものだから、こんな名をつけたのだという。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi480">四七</a></h4></div>
<br />
　ある人が<ruby><rb>清水</rb><rp>（</rp><rt>きよみず</rt><rp>）</rp></ruby>へお<ruby><rb>詣</rb><rp>（</rp><rt>まい</rt><rp>）</rp></ruby>りをしたとき、年寄りの<ruby><rb>尼</rb><rp>（</rp><rt>あま</rt><rp>）</rp></ruby>に道連れになったことがあったが、尼は途中「くさめ、くさめ」と言いながら歩くので、「尼さん何をそんなに言っていらっしゃるのですか」と問うたけれど返事もせずに、やはり言いつづけていたのを、たびたび問われて腹を立てて、「え、鼻のつまったときに、このおまじないをしないと死ぬと言いますから、乳をお飲ませ申した<ruby><rb>方</rb><rp>（</rp><rt>かた</rt><rp>）</rp></ruby>が<ruby><rb>比叡山</rb><rp>（</rp><rt>ひえいざん</rt><rp>）</rp></ruby>に<ruby><rb>児</rb><rp>（</rp><rt>ちご</rt><rp>）</rp></ruby>になっておいであそばすのが、今日でもお鼻をつまらせてはおいでにならぬかと思ってこういうのですよ」と言った。珍しく殊勝な<ruby><rb>志</rb><rp>（</rp><rt>こころざし</rt><rp>）</rp></ruby>ではないか。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi490">四八</a></h4></div>
<br />
　<ruby><rb>葉室中納言光親卿</rb><rp>（</rp><rt>はむろちゅうなごんみつちかきょう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（藤原光親）</span>が、<ruby><rb>後鳥羽院</rb><rp>（</rp><rt>ごとばいん</rt><rp>）</rp></ruby>の<ruby><rb>最勝会</rb><rp>（</rp><rt>さいしょうえ</rt><rp>）</rp></ruby>の講式の<ruby><rb>奉行</rb><rp>（</rp><rt>ぶぎょう</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（公事を執行すること）</span>で<ruby><rb>伺候</rb><rp>（</rp><rt>しこう</rt><rp>）</rp></ruby>して邸前へお召しがあって、お<ruby><rb>膳部</rb><rp>（</rp><rt>ぜんぶ</rt><rp>）</rp></ruby>を出して<ruby><rb>御馳走</rb><rp>（</rp><rt>ごちそう</rt><rp>）</rp></ruby>をたまわった。食い散らしたお<ruby><rb>重</rb><rp>（</rp><rt>じゅう</rt><rp>）</rp></ruby>をそばの<ruby><rb>御簾</rb><rp>（</rp><rt>みす</rt><rp>）</rp></ruby>の中へ押し入れて御前を退出した。女房たちは「まあ、きたならしい、だれに残しておいてくれようとでもいうのかしら」と言い合ったので、院は「古式に<ruby><rb>故実</rb><rp>（</rp><rt>こじつ</rt><rp>）</rp></ruby><span class="sho1" style="font-size: small;">（昔の儀式や作法などの規定）</span>の<ruby><rb>心得</rb><rp>（</rp><rt>こころえ</rt><rp>）</rp></ruby>のあるやり方のりっぱなものである」と繰りかえし繰りかえし御感心なすったということであった。<br />
<br />
<br />
<div class="jisage_3" style="margin-left: 3em"><h4 class="naka-midashi"><a class="midashi_anchor" id="midashi500">四九</a></h4></div>
<br />
　老年になったら仏道を心がけようと待っていてはならない。古い<ruby><rb>墳</rb><rp>（</rp><rt>つか</rt><rp>）</rp></ruby>の多くは少年の人のものである。思いがけない病を得て、ふいにこの世を去ろうとするときになって、やっと過ぎてきた生涯の誤っていたことに気づくであろう。誤りというのは<ruby><rb>他事</rb><rp>（</rp><rt>よそごと</rt><rp>）</rp></ruby>ではない。急を要することをあとまわしにし、あとまわしでよいことをいそいで、過ぎてきたことがくやしいのである。そのときに後悔したって間に合うものでもあるまい。<br />
　人間はただ無常が身に切迫していることを心にはっきりと認識して、瞬間も忘れずにいなければなるまい。そうしたならば、この世の<ruby><rb>濁</rb><rp>（</rp><rt>にご</rt><rp>）</rp></ruby>りに染まることも薄く、仏の道をつとめる心もしんけんにならずにはいまい。昔の高僧は、人が来てさまざまの用談をしかけたとき、「ただ今<ruby><rb>火急</rb><rp>（</rp><rt>かきゅう</rt><rp>）</rp></ruby>の要事があって、もう今日明日に迫っている」と言って、相手の話には耳も貸さないで念仏して、ついに<ruby><rb>往生</rb><rp>（</rp><rt>おうじょう</rt><rp>）</rp></ruby>をとげたと、<ruby><rb>永観律師</rb><rp>（</rp><rt>ようかんりっし</rt><rp>）</rp></ruby>の往生<ruby><rb>十因</rb><rp>（</rp><rt>じゅういん</rt><rp>）</rp></ruby>という書物にある。<ruby><rb>心戒</rb><rp>（</rp><rt>しんかい</rt><rp>）</rp></ruby>といった聖僧は、この世がほんの仮りの宿のようであると痛感して、静かに尻をおろして休むこともなく、<ruby><rb>平生</rb><rp>（</rp><rt>へいぜい</rt><rp>）</rp></ruby>ちょっと腰を曲げてかがんでばかりいたそうである。<br />
<br />
"""
