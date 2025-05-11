/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import me.andannn.aozora.core.database.Tables.SAVED_BOOK_TABLE

internal object SavedBookColumn {
    const val BOOK_ID = "book_id"
    const val CREATED_DATE = "created_date"
}

@Entity(
    tableName = SAVED_BOOK_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = [BookColumns.BOOK_ID],
            childColumns = [SavedBookColumn.BOOK_ID],
        ),
    ],
)
data class SavedBookEntity(
    @PrimaryKey
    @ColumnInfo(name = SavedBookColumn.BOOK_ID)
    val bookId: String,
    @ColumnInfo(name = SavedBookColumn.CREATED_DATE)
    val createdDate: Long,
)
