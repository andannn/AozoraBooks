/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.andannn.aozora.core.database.Tables.BOOK_TABLE

internal object BookColumns {
    const val BOOK_ID = "book_id"
    const val GROUP_ID = "group_id"
    const val TITLE = "title"
    const val TITLE_KANA = "title_kana"
    const val AUTHOR = "author"
    const val AUTHOR_URL = "author_url"
    const val ZIP_URL = "zip_url"
    const val HTML_URL = "html_url"
    const val SAVED_DATE = "saved_date"
}

@Entity(tableName = BOOK_TABLE)
data class BookEntity(
    @PrimaryKey
    @ColumnInfo(name = BookColumns.BOOK_ID)
    val bookId: String,
    @ColumnInfo(name = BookColumns.GROUP_ID)
    val groupId: String,
    @ColumnInfo(name = BookColumns.TITLE)
    val title: String,
    @ColumnInfo(name = BookColumns.TITLE_KANA)
    val titleKana: String,
    @ColumnInfo(name = BookColumns.AUTHOR)
    val author: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_URL)
    val authorUrl: String?,
    @ColumnInfo(name = BookColumns.ZIP_URL)
    val zipUrl: String?,
    @ColumnInfo(name = BookColumns.HTML_URL)
    val htmlUrl: String?,
    @ColumnInfo(name = BookColumns.SAVED_DATE)
    val savedDateInEpochMillisecond: Long,
)
