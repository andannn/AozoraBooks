package me.andannn.aosora.core.pagesource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import me.andannn.aosora.core.common.model.AozoraBookCard
import me.andannn.aosora.core.common.model.BookMeta
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.pagesource.page.generatePageSequence
import me.andannn.aosora.core.parser.AozoraBlockParser
import me.andannn.aosora.core.common.model.BookModel
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.io.path.useLines

/**
 * Create book source from [card].
 */
suspend fun createBookSource(
    scope: CoroutineScope,
    settledPageFLow: Flow<AozoraPage?>,
    card: AozoraBookCard,
): BookPageSource<AozoraPage> {
    val dictionary: Path = getCachedPatchById(card.id)
    val cachedBook = getCachedBookModel(dictionary)
    val bookModel = cachedBook ?: card.downloadBookTo(dictionary)
    return AozoraBookPageSource(
        scope,
        settledPageFLow,
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
    scope: CoroutineScope,
    settledPageFLow: Flow<AozoraPage?>,
    private val bookModel: BookModel,
    private val useHtmlFirst: Boolean = true,
) : LazyBookPageSource<AozoraPage>(scope, settledPageFLow) {
    init {
        if (bookModel.contentHtmlPath == null && bookModel.contentPlainTextPath == null) {
            throw IllegalArgumentException("Either contentHtmlPath or contentPlainTextPath must be specified.")
        }
    }
//
//    override fun pageSource(meta: PageMetaData): Sequence<AozoraPage> = sequence {
//        val parser: AozoraBlockParser
//        val filePath: Path
//        val isHtml: Boolean
//        if (useHtmlFirst) {
//            if (bookModel.contentHtmlPath != null) {
//                parser = createBlockParser(HtmlLineParser)
//                filePath = bookModel.contentHtmlPath
//                isHtml = true
//            } else if (bookModel.contentPlainTextPath != null) {
//                parser = createBlockParser(PlainTextLineParser)
//                filePath = bookModel.contentPlainTextPath
//                isHtml = false
//            } else {
//                throw IllegalArgumentException("Either contentHtmlPath or contentPlainTextPath must be specified.")
//            }
//        } else {
//            if (bookModel.contentPlainTextPath != null) {
//                parser = createBlockParser(PlainTextLineParser)
//                filePath = bookModel.contentPlainTextPath
//                isHtml = false
//            } else if (bookModel.contentHtmlPath != null) {
//                parser = createBlockParser(HtmlLineParser)
//                filePath = bookModel.contentHtmlPath
//                isHtml = true
//            } else {
//                throw IllegalArgumentException("Either contentHtmlPath or contentPlainTextPath must be specified.")
//            }
//        }
//        createSource(meta, parser, filePath, isHtml)
//    }

    private suspend fun SequenceScope<AozoraPage>.createSource(
        meta: PageMetaData,
        blockParser: AozoraBlockParser,
        filePath: Path,
        isHtml: Boolean
    ) {
        Paths.get(filePath.toString()).useLines(
            charset = Charset.forName("Shift_JIS"),
            block = {
                generatePageSequence(
                    aozoraBlockParser = blockParser,
                    lineSequence = it.mapLineAddTrillingT(trilling = if (!isHtml) "\n" else ""),
                    meta = meta
                )
            }
        )
    }

    private fun Sequence<String>.mapLineAddTrillingT(trilling: String = "") = map {
        it + trilling
    }

    override fun generatePageFlowBefore(): Flow<AozoraPage> {
        TODO("Not yet implemented")
    }

    override fun generatePageFlowAfter(): Flow<AozoraPage> {
        TODO("Not yet implemented")
    }
}

private fun getCachedPatchById(id: String): Path {
    return Path("/book/$id")
}

/**
 * download book to [folder].
 *
 * 1. download zip and html from aozora website.
 * 2. unzip.
 * 3. parse book metadata and save json to local file named ".meta"
 * 4. convert **main content** of html and plain-text from shift-jis to utf8 and save them to local file named ".utf8.plaintxt" and ".utf8.html"
 *
 * @throws IllegalArgumentException If htmlUrl is null.
 */
private suspend fun AozoraBookCard.downloadBookTo(folder: Path): BookModel {
    downloadAndUnZip(zipUrl, htmlUrl, folder)

    val htmlPath = SystemFileSystem.list(folder).firstOrNull { it.name.endsWith(".html") }
    val plainTextPath = SystemFileSystem.list(folder).firstOrNull { it.name.endsWith(".txt") }

// TODO : parse meta from html or plain text.
// TODO : get all illustration files.

    val (convertedHtmlPath, convertedTextPath) = coroutineScope {
        val htmlDeferred = async {
            htmlPath?.let { convertHtmlMainContentToUtf8(it, Path(HTML_FILE_NAME)) }
        }

        val plainTextDeferred = async {
            plainTextPath?.let {
                convertPlainTextMainContentToUtf8(
                    folder,
                    Path(PLAIN_TEXT_FILE_NAME)
                )
            }
        }

        htmlDeferred to plainTextDeferred
    }

    return BookModel(
        meta = BookMeta(
            title = "dictionary.name",
            subtitle = null,
            author = "Unknown",
        ),
        contentHtmlPath = convertedHtmlPath.await(),
        contentPlainTextPath = convertedTextPath.await(),
        illustrationPath = emptyList(),
    )
}

/**
 * get book model from catch.
 *
 * @param path Path of the book folder.
 */
private fun getCachedBookModel(path: Path): BookModel? {
    if (!SystemFileSystem.exists(path)) {
        return null
    }
    var meta: BookMeta? = null
    var contentPlainTextPath: Path? = null
    var contentHtmlPath: Path? = null
    val illustrationPathList: MutableList<Path> = mutableListOf()

    SystemFileSystem.list(path).forEach {
// TODO: parse meta from file
        if (it.name == META_FILE_NAME) {
            meta = BookMeta(
                title = "dictionary.name",
                subtitle = null,
                author = "Unknown",
            )
        }
        if (it.name == PLAIN_TEXT_FILE_NAME) {
            contentPlainTextPath = it
        }

        if (it.name == HTML_FILE_NAME) {
            contentHtmlPath = it
        }

        if (it.name.endsWith(".png")) {
            illustrationPathList.add(it)
        }
    }

    if (meta == null) {
        return null
    }
    return BookModel(
        meta = meta,
        contentHtmlPath = contentHtmlPath,
        contentPlainTextPath = contentPlainTextPath,
        illustrationPath = illustrationPathList,
    )
}

private suspend fun downloadAndUnZip(zipUrl: String, htmlUrl: String?, path: Path) =
    coroutineScope {

    }

/**
 * Convert plain txt main content to utf8.
 *
 * @param path Path of the plain text file.
 * @param target Path of the converted plain text file.
 *
 * @return Path of the converted plain text file.
 */
private suspend fun convertPlainTextMainContentToUtf8(path: Path, target: Path): Path? {
    return null
}

/**
 * Convert html main content to utf8.
 *
 * @param path Path of the html file.
 * @param target Path of the converted html file.
 *
 * @return Path of the converted html .
 */
private suspend fun convertHtmlMainContentToUtf8(path: Path, target: Path): Path? {
    return null
}

private const val META_FILE_NAME = ".meta"
private const val PLAIN_TEXT_FILE_NAME = ".utf8.plainText"
private const val HTML_FILE_NAME = ".utf8.html"