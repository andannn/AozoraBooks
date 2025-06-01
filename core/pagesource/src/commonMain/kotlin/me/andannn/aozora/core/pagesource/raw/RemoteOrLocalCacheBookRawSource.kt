/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.raw

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.TextNode
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.json.Json
import me.andannn.aozora.core.domain.exceptions.DownloadBookFailedException
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.CachedBookModel
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.parser.DefaultAozoraBlockParser
import me.andannn.aozora.core.pagesource.parser.html.HtmlLineParser
import me.andannn.aozora.core.pagesource.parser.html.matchers.HeadingMatcher
import me.andannn.aozora.core.pagesource.parser.html.parseAsHtmlNodes
import me.andannn.aozora.core.pagesource.parser.lineSequence
import me.andannn.aozora.core.pagesource.util.validBlock
import me.andannn.core.util.downloadTo
import me.andannn.core.util.readString
import me.andannn.core.util.unzipTo
import org.koin.mp.KoinPlatform.getKoin

private const val TAG = "RemoteOrLocalCacheBookR"

/**
 * Get source from local cached file or fetch from remote.
 */
internal class RemoteOrLocalCacheBookRawSource(
    card: CachedBookModel,
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    private val cacheDictionary: Path = getCachedPatchById(card.id),
) : BookRawSource {
    private val bookModelStateFlow = MutableStateFlow<SourceState>(SourceState.Loading)

    init {
        scope.launch(dispatcher) {
            try {
                bookModelStateFlow.value =
                    SourceState.Success(createBookRawSource(card, cacheDictionary))
            } catch (e: Exception) {
                Napier.e { "Exception thrown when create book raw source. $e" }
                bookModelStateFlow.value = SourceState.Error(e)
            }
        }
    }

    override fun getRawSource(): Flow<AozoraBlock> =
        flow {
            val parser = DefaultAozoraBlockParser(HtmlLineParser)

            val bookModel = waitBookModelOrThrow()
            val filePath =
                bookModel.contentHtmlPath
                    ?: throw IllegalArgumentException("Either contentHtmlPath or contentPlainTextPath must be specified.")

            val source = SystemFileSystem.source(filePath)
            emitAll(
                source
                    .buffered()
                    .lineSequence()
                    .map { parser.parseLineAsBlock(it) }
                    .validBlock()
                    .asFlow()
                    .onCompletion {
                        source.close()
                    },
            )
        }

    override suspend fun getBookInfo(): BookInfo {
        val bookModel = waitBookModelOrThrow()
        return bookModel.info
    }

    override suspend fun getImageUriByPath(path: String): Path? {
        val bookModel = waitBookModelOrThrow()
        val illustrationPath =
            bookModel.illustrationPath.firstOrNull { it.name == path }
                ?: return null
        return illustrationPath
    }

    private suspend fun waitBookModelOrThrow(): BookModel {
        val state = bookModelStateFlow.first { it != SourceState.Loading }
        when (state) {
            is SourceState.Error -> throw state.e
            is SourceState.Success -> return state.source
            SourceState.Loading -> error("Never")
        }
    }
}

private sealed interface SourceState {
    data class Error(
        val e: Exception,
    ) : SourceState

    data class Success(
        val source: BookModel,
    ) : SourceState

    data object Loading : SourceState
}

private suspend fun createBookRawSource(
    card: CachedBookModel,
    cacheDictionary: Path,
): BookModel {
    val cachedBook = getCachedBookModel(cacheDictionary)
    if (cachedBook != null) {
        return cachedBook
    }

    try {
        card.downloadBookTo(cacheDictionary)
    } catch (e: Exception) {
        Napier.e(tag = TAG) { "Download failed $e" }
        throw DownloadBookFailedException(card.title)
    }
    return getCachedBookModel(cacheDictionary) ?: throw DownloadBookFailedException(card.title)
}

/**
 * Get cached path of the book.
 */
internal expect fun getCachedPatchById(id: String): Path

/**
 * download book to [folder].
 *
 * 1. download zip and html from aozora website.
 * 2. unzip.
 * 3. parse book metadata and save json to local file named ".meta"
 * 4. convert **androidMain content** of html and plain-text from shift-jis to utf8 and save them to local file named ".utf8.plaintxt" and ".utf8.html"
 *
 * @throws IllegalArgumentException If htmlUrl is null.
 */
private suspend fun CachedBookModel.downloadBookTo(folder: Path) {
    downloadAndUnZip(zipUrl, htmlUrl, folder)

    val htmlPath = SystemFileSystem.list(folder).firstOrNull { it.name.endsWith(".html") }

    htmlPath?.let { processParseHtml(it, folder) }
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
    var meta: BookInfo? = null
    var contentHtmlPath: Path? = null
    val illustrationPathList: MutableList<Path> = mutableListOf()

    SystemFileSystem.list(path).forEach {
        if (it.name == META_FILE_NAME) {
            val metaJson = SystemFileSystem.source(it).buffered().readString()
            meta = Json.decodeFromString(metaJson)
        }

        if (it.name == HTML_FILE_NAME) {
            contentHtmlPath = it
        }

        if (it.name.endsWith(".png")) {
            illustrationPathList.add(it)
        }
    }

    return BookModel(
        info = meta ?: return null,
        contentHtmlPath = contentHtmlPath,
        illustrationPath = illustrationPathList,
    )
}

private suspend fun downloadAndUnZip(
    zipUrl: String?,
    htmlUrl: String?,
    savePath: Path,
) = coroutineScope {
    val client = getKoin().get<HttpClient>()
    val zipTask =
        zipUrl?.let {
            async {
                val tempZipFilePath = Path("$savePath/temp.zip")
                client.downloadTo(zipUrl, tempZipFilePath)
                tempZipFilePath.unzipTo(savePath)
                SystemFileSystem.delete(tempZipFilePath)
            }
        }

    val htmlTask =
        htmlUrl?.let {
            async {
                val tempHtmlFile = Path("$savePath/$TEMP_HTML")
                client.downloadTo(htmlUrl, tempHtmlFile)
            }
        }

    zipTask?.await()
    htmlTask?.await()
}

/**
 * Convert html androidMain content to utf8.
 *
 * @param path Path of the html file.
 * @param folder Path of the converted html file.
 *
 * @return BookMeta of the html .
 */
internal fun processParseHtml(
    path: Path,
    folder: Path,
) {
    val html = path.readString("Shift_JIS")
    val utf8HtmlFileSink = SystemFileSystem.sink(Path(folder, HTML_FILE_NAME)).buffered()
    val res = Ksoup.parse(html)
    val title = res.select(".title").text()
    val author = res.select(".author").text()
    val children = res.selectFirst(".main_text")?.childNodes() ?: emptyList()
    val bibliographical = res.selectFirst(".bibliographical_information")
    val mainContentBuilder = StringBuilder()

    var lineCount = 0

    var currentLineBuilder: StringBuilder? = null
    val tableOfContentList = mutableListOf<TableOfContent>()
    children
        .asSequence()
        .filterNot { it is TextNode && (it.text().isBlank() || it.text().isEmpty()) }
        .forEach {
            val htmlText = it.toString().replace("\n", "")

            val lineBuilder = currentLineBuilder ?: StringBuilder().also { currentLineBuilder = it }
            lineBuilder.append(htmlText)

            if (it is Element && it.tagName() == "div") {
                // div is considered as a block, add <br> at last.
                val line = "$lineBuilder<br>"
                currentLineBuilder = null
                mainContentBuilder.append(line)
                mainContentBuilder.append("\n")

                lineCount++

                if (line.startsWith("<div") && line.contains("<h")) {
                    val headingElement =
                        HeadingMatcher.match(
                            line.parseAsHtmlNodes().first(),
                        ) as? AozoraElement.Heading ?: error("heading not found")
                    tableOfContentList.add(
                        TableOfContent(
                            headingLevel = headingElement.headingLevel,
                            title = headingElement.text,
                            lineNumber = lineCount,
                        ),
                    )
                }
            } else if (it is Element && it.tagName() == "br") {
                val line = lineBuilder.toString()
                currentLineBuilder = null
                mainContentBuilder.append(line)
                mainContentBuilder.append("\n")

                lineCount++
            }
        }
    utf8HtmlFileSink.use { sink ->
        utf8HtmlFileSink.writeString(mainContentBuilder.toString())
    }

    val model =
        BookInfo(
            title = title,
            subtitle = null,
            author = author,
            blockCount = lineCount,
            bibliographicalInformation = bibliographical.toString(),
            tableOfContentList = tableOfContentList,
        )

    val metaFileSink = SystemFileSystem.sink(Path(folder, META_FILE_NAME)).buffered()

    metaFileSink.use {
        it.writeString(Json.encodeToString(model))
    }

    SystemFileSystem.delete(Path(folder, TEMP_HTML), mustExist = false)
}

private const val META_FILE_NAME = ".meta"
private const val HTML_FILE_NAME = ".utf8.html"
private const val TEMP_HTML = "temp.html"
