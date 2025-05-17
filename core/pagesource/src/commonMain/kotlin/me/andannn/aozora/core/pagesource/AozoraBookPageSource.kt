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
import me.andannn.aozora.core.data.common.CachedBookModel
import me.andannn.aozora.core.pagesource.parser.DefaultAozoraBlockParser
import me.andannn.aozora.core.pagesource.parser.html.HtmlLineParser
import me.andannn.aozora.core.pagesource.parser.lineSequence
import me.andannn.aozora.core.pagesource.raw.BookInfo
import me.andannn.aozora.core.pagesource.raw.BookRawSource
import me.andannn.aozora.core.pagesource.raw.RemoteOrLocalCacheBookRawSource
import me.andannn.aozora.core.pagesource.util.validBlock

/**
 * Book page source.
 */
class AozoraBookPageSource(
    card: CachedBookModel,
    scope: CoroutineScope,
) : CachedLinerPageSource() {
    override val rawSource: BookRawSource =
        RemoteOrLocalCacheBookRawSource(
            card,
            scope,
            dispatcher = Dispatchers.IO,
        )
}

class DummyBookPageSource : CachedLinerPageSource() {
    override val rawSource: BookRawSource =
        object : BookRawSource {
            override suspend fun getRawSource() =
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
        }

    private val dummyHtml: String =
        """
        <br>
        <div class="burasage" style="margin-left: 2em; text-indent: -1em;"> ○食物が人の口に入れば、第一に歯の<ruby><rb>咀嚼</rb><rp>（</rp><rt>そしゃく</rt><rp>）</rp></ruby>を受け、唾液にて<ruby><rb>澱粉</rb><rp>（</rp><rt>でんぷん</rt><rp>）</rp></ruby>を糖分に変化せしめられ、胃に入りて胃筋の機械的作用と胃液の化学作用を受け、それより小腸に入りて腸液と<ruby><rb>膵液</rb><rp>（</rp><rt>すいえき</rt><rp>）</rp></ruby>と胆汁の消化作用を受け、全く消化せしものは門脈を通じて肝臓に入り、ここにて消毒作用を受け、営養分となりて体中に吸収せらる。</div><div class="burasage" style="margin-left: 2em; text-indent: -1em;"> ○何人も折々は断食して胃腸を休息せしむべし。三度の食事時間が来りしとて腹の減らざるに<ruby><rb>強</rb><rp>（</rp><rt>し</rt><rp>）</rp></ruby>いて食物を摂取するは有害なり。</div><div class="burasage" style="margin-left: 2em; text-indent: -1em;">○屠蘇を紅き布に包むは害あり。白布かガーゼにすべし。</div><div class="burasage" style="margin-left: 2em; text-indent: -1em;">○雑煮を作る時は汁の中へ薄切の大根を加うべし。大根は化学作用にて餅を消化せしむ。</div><div class="burasage" style="margin-left: 2em; text-indent: -1em;"> ○何時にても餅を食したる後は大根かあるいは大根<ruby><rb>卸</rb><rp>（</rp><rt>おろ</rt><rp>）</rp></ruby>しを喫すべし。</div><br>
        """.trimIndent()
}
