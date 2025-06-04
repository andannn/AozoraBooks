/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.database.Tables
import me.andannn.aozora.core.database.embedded.AuthorWithBooks
import me.andannn.aozora.core.database.embedded.BookEntityWithProgress
import me.andannn.aozora.core.database.entity.AuthorColumns
import me.andannn.aozora.core.database.entity.AuthorEntity
import me.andannn.aozora.core.database.entity.BookColumns
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookProgressColumns
import me.andannn.aozora.core.database.entity.BookProgressEntity
import me.andannn.aozora.core.database.entity.SavedBookColumn
import me.andannn.aozora.core.database.entity.SavedBookEntity

@Dao
interface BookLibraryDao {
    /**
     * Insert book entities
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookList(bookEntities: List<BookEntity>)

    /**
     * Insert a author entities
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAuthorList(authorEntities: List<AuthorEntity>)

    @Transaction
    suspend fun upsertBookAndAuthorList(
        bookEntities: List<BookEntity>,
        authorEntities: List<AuthorEntity>,
    ) {
        upsertBookList(bookEntities)
        upsertAuthorList(authorEntities)
    }

    /**
     * Get a book by id
     */
    @Query(
        """
        SELECT * 
        FROM ${Tables.BOOK_TABLE} WHERE ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = :bookId
        """,
    )
    fun getBookById(bookId: String): Flow<BookEntity?>

    /**
     * Get paging source of sorted by kana
     */
    @Query(
        """
        SELECT * 
        FROM ${Tables.BOOK_TABLE} 
        WHERE ${BookColumns.TITLE_SORT_KANA} LIKE :kana || '%'
        ORDER BY ${BookColumns.TITLE_SORT_KANA} ASC
        """,
    )
    fun kanaPagingSource(kana: String): PagingSource<Int, BookEntity>

    /**
     * get paging source of author which start by kana line.
     */
    fun authorPagingSource(kanaList: List<String>): PagingSource<Int, AuthorEntity> = authorPagingSource(buildKanaLineStartQuery(kanaList))

    @RawQuery(observedEntities = [AuthorEntity::class])
    fun authorPagingSource(kanaLineStartQuery: RoomRawQuery): PagingSource<Int, AuthorEntity>

    /**
     * Get a book by id
     */
    @Query(
        """
            SELECT * 
            FROM ${Tables.BOOK_TABLE}
            WHERE ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = :bookId AND ${Tables.BOOK_TABLE}.${BookColumns.AUTHOR_ID} = :authorId
        """,
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
            SELECT * 
            FROM ${Tables.BOOK_TABLE}
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
            SELECT * 
            FROM ${Tables.BOOK_TABLE}
            INNER JOIN ${Tables.SAVED_BOOK_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.BOOK_ID}
            LEFT JOIN ${Tables.BOOK_PROGRESS_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.BOOK_ID}
            WHERE ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.MARK_COMPLETED} == 1
            ORDER BY ${SavedBookColumn.CREATED_DATE} DESC
        """,
    )
    fun getCompleteBooksByDesc(): Flow<List<BookEntityWithProgress>>

    @Query(
        """
        SELECT * 
        FROM ${Tables.AUTHOR_TABLE}
        WHERE ${AuthorColumns.AUTHOR_ID} = :authorId
        """,
    )
    fun getAuthorWithBooks(authorId: String): Flow<AuthorWithBooks?>

    /**
     * Get a saved book by id
     */
    @Query(
        """
        SELECT * 
        FROM ${Tables.BOOK_TABLE}
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

    @Query(
        """
            SELECT b.* 
            FROM ${Tables.BOOK_TABLE} b
            JOIN ${Tables.BOOK_FTS_TABLE} ON b.rowid = ${Tables.BOOK_FTS_TABLE}.rowid
            WHERE ${Tables.BOOK_FTS_TABLE} MATCH :query
        """,
    )
    suspend fun searchBook(query: String): List<BookEntity>

    @Query(
        """
            SELECT b.*
            FROM ${Tables.AUTHOR_TABLE} b
            JOIN ${Tables.AUTHOR_FTS_TABLE} ON b.rowid = ${Tables.AUTHOR_FTS_TABLE}.rowid
            WHERE ${Tables.AUTHOR_FTS_TABLE} MATCH :query
        """,
    )
    suspend fun searchAuthor(query: String): List<AuthorEntity>
}

private fun buildKanaLineStartQuery(kanaList: List<String>): RoomRawQuery {
    val whereClause =
        kanaList.joinToString(" OR ") { "${AuthorColumns.LAST_NAME_SORT_KANA} LIKE '$it%'" }
    val sql =
        "SELECT * FROM ${Tables.AUTHOR_TABLE} WHERE $whereClause ORDER BY ${AuthorColumns.LAST_NAME_SORT_KANA} ASC"
    return RoomRawQuery(sql)
}

internal const val READ_PROGRESS_NONE = -2
internal const val READ_PROGRESS_DONE = -1
