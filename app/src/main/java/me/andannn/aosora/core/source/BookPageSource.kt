package me.andannn.aosora.core.source

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import me.andannn.aosora.core.pager.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pager.generatePageSequence
import me.andannn.aosora.core.parser.createBlockParser
import me.andannn.aosora.core.parser.html.HtmlLineParser
import me.andannn.aosora.core.parser.plaintext.PlainTextLineParser
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.io.path.useLines

/**
 * Meta data of book.
 */
data class BookMeta(
    /**
     * Title of book.
     */
    val title: String,

    /**
     * Subtitle of book which maybe null.
     */
    val subtitle: String?,

    /**
     * Author of book.
     */
    val author: String,
)

/**
 * Book source.
 *
 * @property meta Meta data of book.
 * @property contentHtmlPath Path of html file which contains book content.
 * @property contentPlainTextPath Path of plain text file which contains book content.
 * @property illustrationPath List of path of illustration files.
 */
data class Book(
    val meta: BookMeta,
    val contentHtmlPath: Path?,
    val contentPlainTextPath: Path?,
    val illustrationPath: List<Path> = emptyList(),
)

/**
 * Create book source from [dictionary].
 *
 * @param dictionary Dictionary path of book.
 */
fun createBookSource(
    dictionary: Path,
): BookPageSource {
    var contentPlainTextPath: Path? = null
    var contentHtmlPath: Path? = null
    val illustrationPathList: MutableList<Path> = mutableListOf()
    SystemFileSystem.list(dictionary).forEach {
        if (it.name.endsWith(".txt")) {
            contentPlainTextPath = it
        }

        if (it.name.endsWith(".html")) {
            contentHtmlPath = it
        }

        if (it.name.endsWith(".png")) {
            illustrationPathList.add(it)
        }
    }

    return BookPageSource(
        Book(
            meta = BookMeta(
                title = dictionary.name,
                subtitle = null,
                author = "Unknown",
            ),
            contentHtmlPath = contentHtmlPath,
            contentPlainTextPath = contentPlainTextPath,
            illustrationPath = illustrationPathList,
        )
    )
}

/**
 * Book page source.
 */
class BookPageSource(
    private val book: Book
) {
    init {
        if (book.contentHtmlPath == null && book.contentPlainTextPath == null) {
            throw IllegalArgumentException("Either contentHtmlPath or contentPlainTextPath must be specified.")
        }
    }

    fun pageSource(meta: PageMetaData): Sequence<AozoraPage> = sequence {
        createSource(meta)
    }

    private suspend fun SequenceScope<AozoraPage>.createSource(meta: PageMetaData) {
        val (parser, filePath) = if (book.contentHtmlPath != null) {
            createBlockParser(HtmlLineParser) to book.contentHtmlPath
        } else {
            createBlockParser(PlainTextLineParser) to book.contentPlainTextPath!!
        }

        Paths.get(filePath.toString()).useLines(
            charset = Charset.forName("Shift_JIS"),
            block = {
                generatePageSequence(
                    aozoraBlockParser = parser,
                    lineSequence = it,
                    meta = meta
                )
            }
        )
    }
}

