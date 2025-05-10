/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

import kotlinx.io.files.Path
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Book source.
 *
 * @property info Meta data of book.
 * @property contentHtmlPath Path of html file which contains book content.
 * @property illustrationPath List of path of illustration files.
 */
data class BookModel(
    val info: BookInfo,
    val contentHtmlPath: Path?,
    val illustrationPath: List<Path> = emptyList(),
)

/**
 * Meta data of book.
 */
@Serializable
data class BookInfo(
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
    @SerialName("blockCount")
    val blockCount: Int = 0,
    @SerialName("bibliographicalInformation")
    val bibliographicalInformation: String,
    @SerialName("tableOfContents")
    val tableOfContentList: List<TableOfContent>,
)

@Serializable
data class TableOfContent(
    @SerialName("headingLevel")
    val headingLevel: Int,
    @SerialName("text")
    val title: String,
    @SerialName("lineNumber")
    val lineNumber: Int,
)
