/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import me.andannn.aozora.core.database.Tables

internal object BookProgressColumns {
    const val BOOK_ID = "saved_book_id"
    const val AUTHOR_ID = "saved_author_id"
    const val PROGRESS_BLOCK_INDEX = "progress_block_index"
    const val TOTAL_BLOCK_COUNT = "total_block_count"
    const val UPDATE_EPOCH_MILLISECOND = "update_epoch_millisecond"
    const val MARK_COMPLETED = "mark_completed"
}

@Entity(
    tableName = Tables.BOOK_PROGRESS_TABLE,
    primaryKeys = [
        BookProgressColumns.BOOK_ID,
        BookProgressColumns.AUTHOR_ID,
    ],
)
data class BookProgressEntity(
    @ColumnInfo(name = BookProgressColumns.BOOK_ID)
    val bookId: String,
    @ColumnInfo(name = BookProgressColumns.AUTHOR_ID)
    val authorId: String,
    @ColumnInfo(name = BookProgressColumns.PROGRESS_BLOCK_INDEX)
    val progressBlockIndex: Int,
    @ColumnInfo(name = BookProgressColumns.UPDATE_EPOCH_MILLISECOND)
    val updateEpochMillisecond: Long,
    @ColumnInfo(name = BookProgressColumns.TOTAL_BLOCK_COUNT)
    val totalBlockCount: Int? = null,
    @ColumnInfo(name = BookProgressColumns.MARK_COMPLETED)
    val markCompleted: Boolean? = null,
)
