package me.andannn.aosora.core.common.model

import kotlinx.io.files.Path

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

    /**
     * Content byte size of book.
     */
    val contentByteSize: Long = 0,
)