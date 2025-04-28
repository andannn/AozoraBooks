package me.andannn.aosora.core.pagesource.raw

import android.net.Uri
import androidx.core.net.toUri
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import me.andannn.aosora.core.common.model.AozoraBlock
import me.andannn.aosora.core.common.model.AozoraBookCard
import me.andannn.aosora.core.common.model.BookMeta
import me.andannn.aosora.core.common.model.BookModel
import me.andannn.aosora.core.common.util.downloadTo
import me.andannn.aosora.core.common.util.lineSequence
import me.andannn.aosora.core.common.util.unzip
import me.andannn.aosora.core.parser.createBlockParser

/**
 * Get source from local cached file or fetch from remote.
 */
class RemoteOrCacheBookRawSource(
    card: AozoraBookCard,
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    private val useHtmlFirst: Boolean = true,
    private val cacheDictionary: Path = getCachedPatchById(card.id)
) : BookRawSource {
    private val bookModelStateFlow = MutableStateFlow<SourceState>(SourceState.Loading)

    private var loadedSource: Source? = null
    private var usingHtmlFile: Boolean? = null

    init {
        scope.launch(dispatcher) {
            try {
                bookModelStateFlow.value = SourceState.Success(createBookRawSource(card, cacheDictionary))
            } catch (e: Exception) {
                Napier.e { "Exception thrown when create book raw source. $e" }
                bookModelStateFlow.value = SourceState.Error(e)
            }
        }
    }

    override suspend fun getRawSource(): Flow<AozoraBlock> {
        if (loadedSource != null && usingHtmlFile != null) {
            val parser = createBlockParser(isHtml = useHtmlFirst)
            return loadedSource!!.peek().lineSequence().map { parser.parseLineAsBlock(it) }.asFlow()
        }

        val bookModel = waitBookModelOrThrow()
        val (filePath, usingHtml) = if (useHtmlFirst) {
            bookModel.contentHtmlPath?.let { it to true }
                ?: bookModel.contentPlainTextPath?.let { it to false }
                ?: throw IllegalArgumentException("Either contentHtmlPath or contentPlainTextPath must be specified.")
        } else {
            bookModel.contentPlainTextPath?.let { it to false }
                ?: bookModel.contentHtmlPath?.let { it to true }
                ?: throw IllegalArgumentException("Either contentHtmlPath or contentPlainTextPath must be specified.")
        }

        usingHtmlFile = usingHtml
        val parser = createBlockParser(isHtml = usingHtml)
        return SystemFileSystem.source(filePath)
            .buffered()
            .also { loadedSource = it }
            .peek()
            .lineSequence()
            .map { parser.parseLineAsBlock(it) }
            .asFlow()
    }

    override suspend fun getBookMeta(): BookMeta {
        val bookModel = waitBookModelOrThrow()
        return bookModel.meta
    }

    override suspend fun getImageUriByPath(path: String): Uri? {
        val bookModel = waitBookModelOrThrow()
        val illustrationPath = bookModel.illustrationPath.firstOrNull { it.name == path }
            ?: return null
        return illustrationPath.toString().toUri()
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
    data class Error(val e: Exception) : SourceState
    data class Success(val source: BookModel) : SourceState
    data object Loading : SourceState
}

private suspend fun createBookRawSource(
    card: AozoraBookCard,
    cacheDictionary: Path
): BookModel {
    val cachedBook = getCachedBookModel(cacheDictionary)
    return cachedBook ?: card.downloadBookTo(cacheDictionary)
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

private suspend fun downloadAndUnZip(zipUrl: String, htmlUrl: String?, savePath: Path) =
    coroutineScope {
        val client = HttpClient(OkHttp)
        val zipTask = async {
            val tempZipFilePath = Path("$savePath/temp.zip")
            client.downloadTo(zipUrl, tempZipFilePath)
            tempZipFilePath.unzip(savePath)
            SystemFileSystem.delete(tempZipFilePath)
        }

        val htmlTask = htmlUrl?.let {
            async {
                val tempHtmlFile = Path("$savePath/$TEMP_HTML")
                client.downloadTo(htmlUrl, tempHtmlFile)
            }
        }

        zipTask.await()
        htmlTask?.await()
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
// TODO : parse meta from html or plain text.
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
// TODO : get all illustration files.
    return null
}

private const val META_FILE_NAME = ".meta"
private const val PLAIN_TEXT_FILE_NAME = ".utf8.plainText"
private const val HTML_FILE_NAME = ".utf8.html"
private const val TEMP_HTML = "temp.html"
