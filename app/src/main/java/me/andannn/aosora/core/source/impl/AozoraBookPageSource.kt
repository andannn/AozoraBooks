package me.andannn.aosora.core.source.impl

import kotlinx.io.files.Path
import me.andannn.aosora.core.common.model.AozoraBookCard
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pager.AozoraPage
import me.andannn.aosora.core.pager.generatePageSequence
import me.andannn.aosora.core.parser.AozoraBlockParser
import me.andannn.aosora.core.parser.createBlockParser
import me.andannn.aosora.core.parser.html.HtmlLineParser
import me.andannn.aosora.core.parser.plaintext.PlainTextLineParser
import me.andannn.aosora.core.common.model.BookModel
import me.andannn.aosora.core.source.BookPageSource
import me.andannn.aosora.core.source.downloadBookTo
import me.andannn.aosora.core.source.getCachedBookModel
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.io.path.useLines

/**
 * Create book source from [card].
 */
suspend fun createBookSource(
    card: AozoraBookCard,
): BookPageSource {
    val dictionary: Path = getCachedPatchById(card.id)
    val cachedBook = getCachedBookModel(dictionary)
    val bookModel = cachedBook ?: card.downloadBookTo(dictionary)
    return AozoraBookPageSource(
        bookModel,
        useHtmlFirst = true,
    )
}

/**
 * Book page source.
 *
 * @property bookModel Book source.
 * @property useHtmlFirst If true, use html first. Otherwise, use plain text.
 */
private class AozoraBookPageSource(
    private val bookModel: BookModel,
    private val useHtmlFirst: Boolean = true,
) : BookPageSource {
    init {
        if (bookModel.contentHtmlPath == null && bookModel.contentPlainTextPath == null) {
            throw IllegalArgumentException("Either contentHtmlPath or contentPlainTextPath must be specified.")
        }
    }

    override fun pageSource(meta: PageMetaData): Sequence<AozoraPage> = sequence {
        val (parser, filePath) = if (useHtmlFirst && bookModel.contentHtmlPath != null) {
            createBlockParser(HtmlLineParser) to bookModel.contentHtmlPath
        } else {
            createBlockParser(PlainTextLineParser) to bookModel.contentPlainTextPath!!
        }
        createSource(meta, parser, filePath)
    }

    private suspend fun SequenceScope<AozoraPage>.createSource(
        meta: PageMetaData,
        blockParser: AozoraBlockParser,
        filePath: Path
    ) {
        Paths.get(filePath.toString()).useLines(
            charset = Charset.forName("Shift_JIS"),
            block = {
                generatePageSequence(
                    aozoraBlockParser = blockParser,
                    lineSequence = it.mapLineAddTrillingT(trilling = if (blockParser.parser is PlainTextLineParser) "\n" else ""),
                    meta = meta
                )
            }
        )
    }

    fun Sequence<String>.mapLineAddTrillingT(trilling: String = "") = map {
        it + trilling
    }
}

private fun getCachedPatchById(id: String): Path {
    return Path("/book/$id")
}
