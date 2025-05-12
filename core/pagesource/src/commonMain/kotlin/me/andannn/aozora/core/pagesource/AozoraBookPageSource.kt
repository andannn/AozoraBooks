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
import me.andannn.aozora.core.data.common.BookModelTemp
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
    card: BookModelTemp,
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
        <div class="jisage_3" style="margin-left: 3em"><h3 class="o-midashi"><a class="midashi_anchor" id="midashi110">第一巻</a></h3></div><br>
        <div class="jisage_3" style="margin-left: 3em"><h3 class="o-midashi"><a class="midashi_anchor" id="midashi110">第一巻</a></h3></div><br>
        <div class="jisage_3" style="margin-left: 3em"><h3 class="o-midashi"><a class="midashi_anchor" id="midashi110">第一巻</a></h3></div><br>
        <div class="jisage_3" style="margin-left: 3em"><h3 class="o-midashi"><a class="midashi_anchor" id="midashi110">第一巻</a></h3></div><br>
        <div class="jisage_3" style="margin-left: 3em"><h3 class="o-midashi"><a class="midashi_anchor" id="midashi110">第一巻</a></h3></div><br>
        """.trimIndent()
}
