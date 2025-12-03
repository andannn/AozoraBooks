/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.repository

import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.BookWithProgress
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.LineSpacing
import me.andannn.aozora.core.domain.model.ReadProgress
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.core.domain.model.TopMargin

/**
 * Repository for user setting.
 */
interface UserDataRepository {
    /**
     * Get the font size level.
     */
    fun getFontSizeLevel(): Flow<FontSizeLevel>

    /**
     * Get the font type.
     */
    fun getFontFontType(): Flow<FontType>

    /**
     * Get the top margin.
     */
    fun getTopMargin(): Flow<TopMargin>

    /**
     * Get the line spacing.
     */
    fun getLineSpacing(): Flow<LineSpacing>

    /**
     * Get the reader theme.
     */
    fun getReaderTheme(): Flow<ReaderTheme>

    /**
     * Set the font size level.
     */
    suspend fun setFontSizeLevel(fontSizeLevel: FontSizeLevel)

    /**
     * Set the font type.
     */
    suspend fun setFontType(fontType: FontType)

    /**
     * Set the top margin.
     */
    suspend fun setTopMargin(topMargin: TopMargin)

    /**
     * Set the line spacing.
     */
    suspend fun setLineSpacing(lineSpacing: LineSpacing)

    /**
     * Set the reader theme.
     */
    suspend fun setReaderTheme(readerTheme: ReaderTheme)

    /**
     * Set the progress of book.
     */
    suspend fun setProgressOfBook(
        bookCardId: String,
        authorId: String,
        readProgress: ReadProgress,
    )

    /**
     * Mark book as completed.
     */
    suspend fun markBookAsCompleted(
        bookCardId: String,
        authorId: String,
    )

    /**
     * Mark book as not completed.
     */
    suspend fun markBookAsNotCompleted(bookCardId: String)

    /**
     * Get the progress of book.
     */
    suspend fun getProgress(bookCardId: String): ReadProgress

    /**
     * Get the progress flow of book.
     */
    fun getProgressFlow(bookCardId: String): Flow<ReadProgress>

    /**
     * Get the progress flow of book.
     */
    fun isUserMarkCompletedFlow(bookCardId: String): Flow<Boolean>

    /**
     * Save book to library.
     */
    suspend fun saveBookToLibrary(
        bookId: String,
        authorId: String,
    )

    /**
     * Get all book which is not completed.
     */
    fun getAllNotCompletedBooks(): Flow<List<BookWithProgress>>

    /**
     * Get all completed books.
     */
    fun getAllCompletedBooks(): Flow<List<BookWithProgress>>

    /**
     * Delete saved book.
     */
    suspend fun deleteSavedBook(
        bookId: String,
        authorId: String,
    )

    /**
     * Get saved book by id.
     */
    fun getSavedBookById(
        bookId: String,
        authorId: String,
    ): Flow<AozoraBookCard?>

    fun getBookCache(
        bookId: String,
        authorId: String,
    ): Flow<AozoraBookCard?>

    suspend fun isNdcTableMigrated(): Boolean
}
