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
import me.andannn.aozora.core.database.entity.BookIdWithBookCategory
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
        bookIdWithBookCategories: List<BookIdWithBookCategory>,
    ) {
        upsertBookList(bookEntities)
        upsertAuthorList(authorEntities)
        upsertBookIdWithBookCategoryList(bookIdWithBookCategories)
    }

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
            INNER JOIN ${Tables.SAVED_BOOK_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.BOOK_ID} AND ${Tables.BOOK_TABLE}.${BookColumns.AUTHOR_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.AUTHOR_ID}
            LEFT JOIN ${Tables.BOOK_PROGRESS_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.BOOK_ID} AND ${Tables.BOOK_TABLE}.${BookColumns.AUTHOR_ID} = ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.AUTHOR_ID}
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
            INNER JOIN ${Tables.SAVED_BOOK_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.BOOK_ID} AND ${Tables.BOOK_TABLE}.${BookColumns.AUTHOR_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.AUTHOR_ID}
            LEFT JOIN ${Tables.BOOK_PROGRESS_TABLE} ON ${Tables.BOOK_TABLE}.${BookColumns.BOOK_ID} = ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.BOOK_ID} AND ${Tables.BOOK_TABLE}.${BookColumns.AUTHOR_ID} = ${Tables.BOOK_PROGRESS_TABLE}.${BookProgressColumns.AUTHOR_ID}
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
        SELECT b.*
        FROM ${Tables.BOOK_TABLE} b
        INNER JOIN ${Tables.SAVED_BOOK_TABLE} ON b.${BookColumns.BOOK_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.BOOK_ID} AND b.${BookColumns.AUTHOR_ID} = ${Tables.SAVED_BOOK_TABLE}.${SavedBookColumn.AUTHOR_ID}
        WHERE b.${BookColumns.BOOK_ID} = :bookId AND b.${BookColumns.AUTHOR_ID} = :authorId
        """,
    )
    fun getSavedBookById(
        bookId: String,
        authorId: String,
    ): Flow<BookEntity?>

    /**
     * Delete a saved book
     */
    @Query("DELETE FROM ${Tables.SAVED_BOOK_TABLE} WHERE ${SavedBookColumn.BOOK_ID} = :bookId AND ${SavedBookColumn.AUTHOR_ID} = :authorId")
    suspend fun deleteSavedBook(
        bookId: String,
        authorId: String,
    )

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookIdWithBookCategoryList(categoryList: List<BookIdWithBookCategory>)

    // only for test
    @Query(
        """
            SELECT b.*
            FROM ${Tables.BOOK_ID_WITH_BOOK_CATEGORY_TABLE} a
            INNER JOIN ${Tables.BOOK_TABLE} b ON b.book_id = a.book_id AND b.author_id = a.author_id
            WHERE a.ndc_main_class_num = :ndcMainClassNum 
                AND a.ndc_division_num = :ndcDivisionNum 
                AND a.ndc_section_num = :ndcSectionNum
        """,
    )
    suspend fun getBooksWithClassification(
        ndcMainClassNum: Int,
        ndcDivisionNum: Int,
        ndcSectionNum: Int,
    ): List<BookEntity>

    @Query(
        """
            SELECT b.*
            FROM ${Tables.BOOK_ID_WITH_BOOK_CATEGORY_TABLE} a
            INNER JOIN ${Tables.BOOK_TABLE} b ON b.book_id = a.book_id AND b.author_id = a.author_id
            WHERE a.ndc_main_class_num = :ndcMainClassNum 
                AND a.ndc_division_num = :ndcDivisionNum 
                AND a.ndc_section_num = :ndcSectionNum
        """,
    )
    fun booksOfClassificationPaging(
        ndcMainClassNum: Int,
        ndcDivisionNum: Int,
        ndcSectionNum: Int,
    ): PagingSource<Int, BookEntity>

    fun bookCountOfNdcCategoryFlow(
        ndcMainClassNum: Int,
        ndcDivisionNum: Int?,
        ndcSectionNum: Int?,
    ): Flow<Int> =
        when {
            ndcDivisionNum == null && ndcSectionNum == null ->
                getNdcMainClassBookCount(
                    ndcMainClassNum,
                )

            ndcSectionNum == null && ndcDivisionNum != null ->
                getNdcDivisionBookCount(
                    ndcMainClassNum,
                    ndcDivisionNum,
                )

            ndcSectionNum != null && ndcDivisionNum != null ->
                getNdcSectionBookCount(
                    ndcMainClassNum,
                    ndcDivisionNum,
                    ndcSectionNum,
                )

            else -> throw IllegalArgumentException(
                "Invalid NDC classification parameters provided: " +
                    "mainClassNum=$ndcMainClassNum, " +
                    "divisionNum=$ndcDivisionNum, " +
                    "sectionNum=$ndcSectionNum",
            )
        }

    @Query(
        """
            SELECT COUNT(*) 
            FROM ${Tables.BOOK_ID_WITH_BOOK_CATEGORY_TABLE} 
            WHERE ndc_main_class_num = :ndcMainClassNum
        """,
    )
    fun getNdcMainClassBookCount(ndcMainClassNum: Int): Flow<Int>

    @Query(
        """
            SELECT COUNT(*) 
            FROM ${Tables.BOOK_ID_WITH_BOOK_CATEGORY_TABLE} 
            WHERE ndc_main_class_num = :ndcMainClassNum 
                AND ndc_division_num = :ndcDivisionNum
        """,
    )
    fun getNdcDivisionBookCount(
        ndcMainClassNum: Int,
        ndcDivisionNum: Int,
    ): Flow<Int>

    @Query(
        """
            SELECT COUNT(*) 
            FROM ${Tables.BOOK_ID_WITH_BOOK_CATEGORY_TABLE} 
            WHERE ndc_main_class_num = :ndcMainClassNum 
                AND ndc_division_num = :ndcDivisionNum 
                AND ndc_section_num = :ndcSectionNum
        """,
    )
    fun getNdcSectionBookCount(
        ndcMainClassNum: Int,
        ndcDivisionNum: Int,
        ndcSectionNum: Int,
    ): Flow<Int>
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
