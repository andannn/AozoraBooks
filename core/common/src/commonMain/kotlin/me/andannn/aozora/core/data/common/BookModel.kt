package me.andannn.aozora.core.data.common

import kotlinx.io.files.Path
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Book source.
 *
 * @property meta Meta data of book.
 * @property contentHtmlPath Path of html file which contains book content.
 * @property contentPlainTextPath Path of plain text file which contains book content.
 * @property illustrationPath List of path of illustration files.
 */
data class BookModel(
    val meta: BookMeta,
    val contentHtmlPath: Path?,
    val contentPlainTextPath: Path?,
    val illustrationPath: List<Path> = emptyList(),
)

/**
 * Meta data of book.
 */
@Serializable
data class BookMeta(
    /**
     * Title of book.
     */
    @SerialName("title")
    val title: String,
    /**
     * Subtitle of book which maybe null.
     */
    @SerialName("subtitle")
    val subtitle: String?,
    /**
     * Author of book.
     */
    @SerialName("author")
    val author: String,
    /**
     * Content byte size of book.
     */
    @SerialName("content_length")
    val contentByteSize: Long = 0,
)
