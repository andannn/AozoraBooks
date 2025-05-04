package me.andannn.aozora.core.pagesource

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.io.files.Path
import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.BookMeta
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.page.builder.createPageBuilder
import me.andannn.aozora.core.pagesource.page.createPageFlowFromSequence
import me.andannn.aozora.core.pagesource.raw.BookRawSource
import me.andannn.aozora.core.parser.createBlockParser
import me.andannn.aozora.core.parser.lineSequence
import me.andannn.core.util.asSource

object DummyBookPageSource {
    fun createSimpleDummyBookPageSource(): BookPageSource {
        val pageList = mutableListOf<AozoraPage>()
        return object : BookPageSource {
            override fun getPagerSnapShotFlow(
                meta: PageMetaData,
                startProgress: Long,
            ): Flow<PagerSnapShot> {
                val aozoraBlockParser = createBlockParser(true)

                fun pageFlow() =
                    createPageFlowFromSequence(
                        blockSequenceFlow =
                            dummyHtml
                                .asSource()
                                .lineSequence()
                                .map { aozoraBlockParser.parseLineAsBlock(it) }
                                .asFlow(),
                        builderFactory = {
                            createPageBuilder(meta)
                        },
                    )
                return pageFlow().map {
                    pageList.add(it)
                    PagerSnapShot(
                        pageList = pageList.toImmutableList(),
                        initialIndex = 0,
                        snapshotVersion = 0,
                    )
                }
            }

            override fun dispose() {
            }
        }
    }

    fun createDummySequenceCachedSource(scope: CoroutineScope) =
        object : CachedLinerPageSource(
            rawSource =
                object : BookRawSource {
                    override suspend fun getRawSource(): Flow<AozoraBlock> {
                        val dummySource = dummyHtml.asSource()
                        return dummySource
                            .peek()
                            .lineSequence()
                            .map {
                                createBlockParser(true).parseLineAsBlock(it)
                            }.asFlow()
                    }

                    override suspend fun getImageUriByPath(path: String): Path? {
                        TODO("Not yet implemented")
                    }

                    override suspend fun getBookMeta(): BookMeta {
                        TODO("Not yet implemented")
                    }

                    override fun dispose() {
                        TODO("Not yet implemented")
                    }
                },
        ) {}
}

private val dummyHtml: String
    get() = """
<br />
　時は現代。陰暦八月十五日のゆうぐれ。<br />
　満州、大連市外の村はずれにある李中行の家。すべて農家の作りにて、家内の大部分は土間。正面には出入りの扉ありて、下のかたの壁には簑笠などをかけ、その下には<ruby><rb>鋤</rb><rp>（</rp><rt>すき</rt><rp>）</rp></ruby>またや<ruby><rb>鍬</rb><rp>（</rp><rt>くわ</rt><rp>）</rp></ruby>などの農具を置いてあり。その傍らには大いなる<ruby><rb>土竃</rb><rp>（</rp><rt>どがま</rt><rp>）</rp></ruby>ありて、棚には茶碗、小皿、鉢などの食器をのせ、竃のそばには焚物用の<ruby><rb>高粱</rb><rp>（</rp><rt>コーリャン</rt><rp>）</rp></ruby>を<span class="notes">［＃「<ruby><rb>高粱</rb><rp>（</rp><rt>コーリャン</rt><rp>）</rp></ruby>を」は底本では「<ruby><rb>高梁</rb><rp>（</rp><rt>コーリャン</rt><rp>）</rp></ruby>を」］</span>束ねたるを積み、水を入れたるバケツなどもあり。よきところに木卓を置き、おなじく三四脚の<ruby><rb>木榻</rb><rp>（</rp><rt>もくとう</rt><rp>）</rp></ruby>あり。下のかたには窓あり。上のかたは寝室のこころにて、ここにも出入りの扉あり。家の外には柳の大樹、その下に石の井戸あり。うしろは高粱の<span class="notes">［＃「高粱の」は底本では「高梁の」］</span>畑を隔てて、大連市街の灯が遠くみゆ。<br />
（家の妻柳、四十余歳。高粱を<span class="notes">［＃「高粱を」は底本では「高梁を」］</span>折りくべて、竃の下に火を焚いている。家内は薄暗く、水の音遠くきこゆ。下のかたより家の娘阿香、十七八歳、印刷工場の女工のすがたにて、高田圭吉と連れ立ちて出づ。高田は二十四五歳、おなじく印刷職工の姿。）<br />
高田　（窓から内を覗く。）<ruby><rb>阿母</rb><rp>（</rp><rt>おっか</rt><rp>）</rp></ruby>さんは火を焚いているようだ。じゃあ、ここで別れるとしよう。<br />
阿香　あら、内へ<ruby><rb>這入</rb><rp>（</rp><rt>はい</rt><rp>）</rp></ruby>らないの。<br />
高田　まあ、止そう。毎晩のように尋ねて行くと、お<ruby><rb>父</rb><rp>（</rp><rt>とっ</rt><rp>）</rp></ruby>さんや阿母さんにうるさがられる。第一、僕も極まりが悪いからな。<br />
阿香　かまわないわ。始終遊びに来るんじゃありませんか。お寄りなさいよ。<br />
高田　始終遊びに来る<ruby><rb>家</rb><rp>（</rp><rt>うち</rt><rp>）</rp></ruby>でも、この頃はなんだか極まりが悪くなった。まあ、帰るとしよう。<br />
阿香　いいじゃありませんか。（袖をひく。）今夜は十五夜だから、一緒にお月様を拝みましょうよ。<br />
高田　（躊躇して。）それにしても、まあ、ゆう飯を食ってから出直して来ることにしよう。<br />
阿香　じゃあ、<ruby><rb>屹</rb><rp>（</rp><rt>きっ</rt><rp>）</rp></ruby>とね。<br />
高田　むむ。（空をみる。）今夜は好い月が出そうだ。<br />
阿香　お月様にお供え物をして待っていますよ。<br />
（高田はうなずいて、下のかたへ引返して去る。阿香はそれを見送りながら、正面へまわりて扉を叩く。）<br />
柳　（みかえる。）誰だえ。お父さんかえ。<br />
阿香　わたしですよ。<br />
柳　戸は開いているよ。お這入り……。<br />
阿香　（扉をあけて入る。）あら、暗いことね。まだ<ruby><rb>燈火</rb><rp>（</rp><rt>あかり</rt><rp>）</rp></ruby>をつけないの。<br />
柳　いつの間にか暗くなったね。<br />
阿香　町の方じゃあ、もう<ruby><rb>疾</rb><rp>（</rp><rt>と</rt><rp>）</rp></ruby>うに電燈がついているわ。<br />
柳　町とここらとは違わあね。あかりをつけないでも、今にもうお月様がおあがりなさるよ。<br />
阿香　それでもあんまり暗いわ。<br />
（阿香は上のかたの一室に入る。柳は竃の下を焚きつけている。表はだんだんに薄明るくなる。下のかたよりこの家のあるじ李中行、五十歳前後、肉と菓子とを入れたる袋を両脇にかかえて出づ。）<br />
李中行　そろそろお月様がおあがりなさると見えて、東の空が明るくなって来た。<br />
柳　（窓から覗く。）お父さんかえ。<br />
<br />
李中行　むむ。今帰ったよ。（正面の扉をあけて入る。）阿香はどうした。<br />
柳　たった今、帰って来ましたよ。時に買い物は……。<br />
李中行　（袋を卓の上に置く。）まあ、どうにか<ruby><rb>斯</rb><rp>（</rp><rt>こ</rt><rp>）</rp></ruby>うにか買うだけの品は買い<ruby><rb>調</rb><rp>（</rp><rt>ととの</rt><rp>）</rp></ruby>えて来たが、むかしと違って、一年増しに何でも値段が高くなるには<em class="sesame_dot">びっくり</em>するよ。<ruby><rb>月餅</rb><rp>（</rp><rt>げっぺい</rt><rp>）</rp></ruby>一つだって、うっかり買われやあしない。<br />
柳　まったく私達の若い時のことを考えると、なんでも相場が高くなって、世の中は暮らしにくくなるばかりだ。それでもこうして生きている以上は、不断はどんなに倹約しても、お正月とか十五夜とかいう時だけは、まあ世間並のことをしないと気が済まないからね。<br />
李中行　そうだ、そうだ。おれもそう思うから、見す見す高い物をこうして買い込んで来たのだ。阿香が帰っているなら、あれに手伝わせて早くお供え物を飾り付けたら好かろう。もうお月さまはお出なさるのだ。<br />
柳　（窓から表を覗く。）今夜はすっかり晴れているから、好いお月さまが拝めるだろう。<br />
李中行　むむ。近年にない十五夜だ。<br />
（阿香は着物を着かえ、小さいランプを手にして、一室より出づ。）<br />
阿香　お父さん。お帰りなさい。<br />
李中行　さあ、みんな買って来たから、早く供えてくれ。<br />
阿香　十五夜のお供え物も高くなったそうですね。<br />
李中行　今もそれを云っていたのだが、だんだん貧乏人泣かせの世の中になるばかりだ。<br />
阿香　（笑う。）おめでたいお月見の晩に、そんな泣き言を云うもんじゃないわ。じゃあ、阿母さん。<br />
柳　あいよ。<br />
（母と娘は上のかたの壁の前に種々の供物をして、月を祭る準備をする。李は疲れたように、<ruby><rb>榻</rb><rp>（</rp><rt>とう</rt><rp>）</rp></ruby>に<span class="notes">［＃「<ruby><rb>榻</rb><rp>（</rp><rt>とう</rt><rp>）</rp></ruby>に」は底本では「<ruby><rb>搨</rb><rp>（</rp><rt>とう</rt><rp>）</rp></ruby>に」］</span>腰をおろしている。）<br />
阿香　お父さん。<ruby><rb>草臥</rb><rp>（</rp><rt>くたび</rt><rp>）</rp></ruby>れたの。<br />
李中行　むむ。何だか<em class="sesame_dot">がっかり</em>してしまった。<br />
柳　町へ買い物に行って来たぐらいで、そんなに<em class="sesame_dot">がっかり</em>するようじゃあ困るね。<br />
李中行　一つは気疲れがしたのだな。近所でありながら、滅多に町の方へ出ないものだから、たまに出て行くと自動車や自転車で危なくってならない。おれはどうしても昔の人間だよ。時に<ruby><rb>中二</rb><rp>（</rp><rt>ちゅうじ</rt><rp>）</rp></ruby>はまだ来ないのかな。<br />
阿香　兄さんは来るかしら。<br />
李中行　来るも来ないもあるものか。十五夜には<ruby><rb>家</rb><rp>（</rp><rt>うち</rt><rp>）</rp></ruby>へ帰って来て、おれたちと一緒に月を拝めと、あれほど云い聞かせて置いたのだから、屹と来るに相違ないよ。<br />
柳　十五夜で、店の方が忙がしいのじゃないかね。<br />
李中行　なに、あいつの勤めている店は本屋だ。おまけに主人は日本人だから、十五夜に係り合があるものか。あいつ、何をしているのかな。<br />
柳　そう云っても奉公の身の上だから、自由に店をぬけ出して来ることは出来ないのかも知れない。<br />
阿香　十五夜だから暇をくれなんて云っても、主人が承知するか<ruby><rb>何</rb><rp>（</rp><rt>ど</rt><rp>）</rp></ruby>うだか判らないわ。<br />
李中行　でも、去年は来たではないか。<br />
阿香　去年は去年……。今年はどうだか……。ねえ、阿母さん。<br />
柳　中二も主人の気に入って、今年からは月給が五円あがったというから、それだけに仕事の方も忙がしくなったかも知れないからね。<br />
李中行　月給のあがったのは結構だが、それがために十五夜に帰って来られないようでは困るな。<br />
阿香　あら、お父さん。十五夜よりも月給のあがった方が好いわ。<br />
李中行　おまえも今の娘だな。（舌打ちして。）まあ、仕様がない。毎年親子四人が欠かさずに月を拝んでいるのに、今年だけはあいつが欠けるのか。<br />
柳　そう思うと、わたしも何だか寂しいような気もするが、いつまで待ってもいられまい。<br />
阿香　今に高田さんが来ますよ。<br />
柳　お前、約束をしたのかえ。<br />
阿香　きっと来ると云っていましたわ。<br />
李中行　高田さんが来たところで、あの人は他人だ。せがれの代りになるものか。こうと知ったら、町へ出た時にあいつの店へ寄って、もう一度よく念を押してくれば好かったな。<br />
阿香　（それには構わず。）高田さんは何をしているんだろう。<br />
（阿香は表へ出で、柳の下に立って下のかたをみる。表はいよいよ明るくなる。）<br />
柳　（窓から声をかける。）もうお月様はおあがりになるかえ。<br />
阿香　（空をみる。）ええ、もういつの間にかお上りになりましたよ。<br />
柳　じゃあ、もう拝みに出なけりゃあならない。さあ、お父さん。<br />
李中行　中二はまだ帰らないのかなあ。<br />
阿香　高田さんはどうしたんだろうねえ。<br />
（李は妻に促されて、渋々ながら立ち上り、打ち連れて表へ出る。月の光いよいよ明るく、虫の声。）<br />
柳　おお、好いお月さまが出た。十五夜にこんなに晴れたのは、近年にめずらしい。さあ、拝みましょうよ。<br />
（李と、柳、阿香の三人は形をあらため、下のかたの空を仰いで拝す。）<br />
柳　まあ、まあ、これで好い。ことしの月も無事に拝んだ。<br />
李中行　日が暮れたら急に冷えて来た。秋風はやっぱり身にしみるな。<br />
（李と柳は引返して内に入る。阿香はまだ立って下のかたを眺めている。）<br />
柳　お月さまを拝んでしまったら、今夜の御祝儀に一杯お飲みなさいよ。<br />
李中行　今夜はせがれを相手に飲む積りだったが、あいつめ、まだ形をみせない。こうなったら仕様がないから、せめて高田さんでも来ればいいな。（外へ声をかける。）おい、高田さんは<ruby><rb>屹</rb><rp>（</rp><rt>きっ</rt><rp>）</rp></ruby>と来ると云ったのか。<br />
阿香　ええ、ええ、屹と来る筈なんだけれど……。あの人は何をしているんだろう。わたし行って呼んで来るわ。<br />
柳　まあ、いいよ。若い女が夜あるきをするにゃあ及ばない。そのうちに来るだろうよ。<br />
阿香　でも、ちょいと行って来るわ。お父さん。まあそれまではお酒を飲まずに、待っていて下さいよ。（早々に下のかたへ去る。）<br />
柳　とうとう出て行ってしまった。あの子は高田さんばかり恋しがっているんだよ。<br />
李中行　（笑う。）まあ、仕方がない。打っちゃって置け。おれはこの通りの貧乏だから、若い娘を印刷工場へ通わせて置くが、いつまでそうしても置かれない。遅かれ早かれ<ruby><rb>他</rb><rp>（</rp><rt>よそ</rt><rp>）</rp></ruby>へ縁付けなければならないのだ。（又笑う。）高田さんは善い人だよ。<br />
柳　そりゃ私も知っているけれど……。じゃあ、いよいよと云うときには、お前さんも承知してくれるのね。<br />
李中行　むむ、承知するよ。日本人でも何でも構うものか。相手が正直で、よく働く人で、娘も進んで行くというなら、おれは喜んで承知するよ。昔と今とは世の中が違うからな。<br />
柳　お前さんがそう云ってくれれば、わたしも安心だけれど……。<br />
李中行　それだから安心していろよ。はははははは。<br />
（正面の扉をたたく音。）<br />
柳　阿香が帰ったのかしら。（考える。）そんなら戸をたたく筈もないが……。<br />
李中行　それとも、せがれが遣って来たのかな。<br />
（柳は立って扉をあけると、旅すがたの男一人入り来る。男は四十余歳にて、鬚あり。）<br />
柳　（すかし視て。）おまえさんは誰だね。<br />
旅の男　おかみさんはもう私を見忘れましたかね。<br />
柳　はてね。そう云えば、なんだか見たような顔でもあるが……。<br />
旅の男　（笑いながら。）御亭主は覚えていなさるでしょうね。<br />
李中行　（立ちあがって覗く。）成程、見たことがあるようだが……。ちょっと思い出せないな。<br />
旅の男　（しずかに。）わたしは預け物をうけ取りに来たのです。<br />
李中行　（思い出して。）ああ、判った、判った。おまえさんは……あの人だ、あの人だ。<br />
旅の男　ここの<ruby><rb>家</rb><rp>（</rp><rt>うち</rt><rp>）</rp></ruby>に四五日御厄介になったことのある旅の者です。三年<ruby><rb>後</rb><rp>（</rp><rt>のち</rt><rp>）</rp></ruby>の八月十五夜の晩には、必ず再びたずねて来ますからと云って、小さい箱をあずけて行った筈ですが……。<br />
柳　ああ、わたしも思い出した。三年前の雨のふる晩に、泊めてくれと云って来た人だ。<br />
李中行　見識らない人ではあるし、夜は更けている。むやみに泊めるわけには行かないと一旦は断ったのを、お前さんは無理に泊めてくれと云って、とうとうこの土間の隅に寝込んでしまったのだ。<br />
柳　おまけにその明くる朝から病気になったと云い出して、私達もどんなに心配したか知れやあしない。<br />
旅の男　まったくあの時には飛んだ御厄介になりました。それから四五日もここの家に寝かして貰って、再び元のからだになったのです。（頭を下げる。）今晩あらためてお礼を申上げます。<br />
李中行　わたしの方では忘れていたが、成程そのときに、三年<ruby><rb>後</rb><rp>（</rp><rt>のち</rt><rp>）</rp></ruby>の十五夜の晩には再びたずねて来ると云ったようだ。<br />
旅の男　その約束の通りに、今夜再び来ました。<br />
李中行　そう聞くと、なんだか懐かしいようでもある。まあ、まあ、ここへ掛けなさい。<br />
（旅の男は会釈しただけで、やはり立っている。）<br />
李中行　そこでお前さんは、あれから三年の間、どこを歩いていなすったのだ。<br />
旅の男　それからそれへと旅の空をさまよっていました。いや、そんなお話をしていると、長くなります。おあずけ申して置いた箱を受取って、今夜はこのまま帰るとしましょう。<br />
李中行　そんなに急ぎなさるのかね。<br />
旅の男　急がないでもありません。どうぞあの箱を直ぐにお渡しください。<br />
李中行　はは、わたし達は正直物だ。預かり物は大切に仕舞ってあるから、安心しなさい。（柳に。）さあ、持って来て早く渡して遣るが好い。<br />
（柳は一室に入る。旅の男はやはり立っている。やがて奥にて柳の声。）<br />
柳、ちょいと、お前さん……。<br />
李中行　なんだ。<br />
柳　どうも不思議なことがあるんですよ。<br />
"""
