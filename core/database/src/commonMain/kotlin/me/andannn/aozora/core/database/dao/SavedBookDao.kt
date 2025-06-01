/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.database.Tables
import me.andannn.aozora.core.database.embedded.BookEntityWithProgress
import me.andannn.aozora.core.database.entity.BookColumns
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookProgressColumns
import me.andannn.aozora.core.database.entity.BookProgressEntity
import me.andannn.aozora.core.database.entity.SavedBookColumn
import me.andannn.aozora.core.database.entity.SavedBookEntity

/**
 * The DAO for [BookEntity]
 */
@Dao
interface SavedBookDao {
    /**
     * Insert a saved book
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookList(bookEntities: List<BookEntity>)

    /**
     * Get a book by id
     */
    @Query("SELECT * FROM ${Tables.BOOK_TABLE} WHERE ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = :bookId")
    fun getBookById(bookId: String): Flow<BookEntity?>

    /**
     * Get a book by id
     */
    @Query(
        "SELECT * FROM ${Tables.BOOK_TABLE} WHERE ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = :bookId AND ${Tables.BOOK_TABLE}.${BookColumns.AUTHOR_ID} = :authorId",
    )
    fun getBookByBookIdAndAuthorId(
        bookId: String,
        authorId: String,
    ): Flow<BookEntity?>

    /**
     * Get all saved books
     */
    @Query(
        """
            SELECT * FROM ${Tables.BOOK_TABLE}
            INNER JOIN ${Tables.SAVED_BOOK_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.BOOK_ID}
            LEFT JOIN ${Tables.BOOK_PROGRESS_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.BOOK_ID}
            WHERE ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.MARK_COMPLETED} != 1 OR ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.MARK_COMPLETED} IS NULL
            ORDER BY ${SavedBookColumn.CREATED_DATE} DESC
        """,
    )
    fun getNotCompletedBooksByDesc(): Flow<List<BookEntityWithProgress>>

    /**
     * Get all saved books
     */
    @Query(
        """
            SELECT * FROM ${Tables.BOOK_TABLE}
            INNER JOIN ${Tables.SAVED_BOOK_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.BOOK_ID}
            LEFT JOIN ${Tables.BOOK_PROGRESS_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.BOOK_ID}
            WHERE ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.MARK_COMPLETED} == 1
            ORDER BY ${SavedBookColumn.CREATED_DATE} DESC
        """,
    )
    fun getCompleteBooksByDesc(): Flow<List<BookEntityWithProgress>>

    /**
     * Get a saved book by id
     */
    @Query(
        """
        SELECT * FROM ${Tables.BOOK_TABLE}
        WHERE ${BookColumns.BOOK_ID} = :bookId
            AND ${BookColumns.BOOK_ID} IN (SELECT ${SavedBookColumn.BOOK_ID} FROM ${Tables.SAVED_BOOK_TABLE})
    """,
    )
    fun getSavedBookById(bookId: String): Flow<BookEntity?>

    /**
     * Delete a saved book
     */
    @Query("DELETE FROM ${Tables.SAVED_BOOK_TABLE} WHERE ${SavedBookColumn.BOOK_ID} = :bookId")
    suspend fun deleteSavedBook(bookId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSavedBook(savedBookEntity: SavedBookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgressOfBook(entity: BookProgressEntity)

    @Query("SELECT * FROM ${Tables.BOOK_PROGRESS_TABLE} WHERE ${BookProgressColumns.BOOK_ID} = :bookId")
    suspend fun getProgressOfBook(bookId: String): BookProgressEntity?

    @Query("SELECT * FROM ${Tables.BOOK_PROGRESS_TABLE} WHERE ${BookProgressColumns.BOOK_ID} = :bookId")
    fun getProgressOfBookFlow(bookId: String): Flow<BookProgressEntity?>
}

internal const val READ_PROGRESS_NONE = -2
internal const val READ_PROGRESS_DONE = -1
