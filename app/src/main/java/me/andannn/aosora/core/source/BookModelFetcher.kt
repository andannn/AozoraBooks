package me.andannn.aosora.core.source

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import me.andannn.aosora.core.common.model.AozoraBookCard
import me.andannn.aosora.core.common.model.BookMeta
import me.andannn.aosora.core.common.model.BookModel

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
suspend fun AozoraBookCard.downloadBookTo(folder: Path): BookModel {
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

fun getCachedBookModel(path: Path): BookModel? {
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
