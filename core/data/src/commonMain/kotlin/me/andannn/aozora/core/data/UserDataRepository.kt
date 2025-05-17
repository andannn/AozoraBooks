/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data

import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.data.common.BookModelTemp
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.ReadProgress
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.data.common.TopMargin

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
        readProgress: ReadProgress,
    )

    /**
     * Get the progress of book.
     */
    suspend fun getProgress(bookCardId: String): ReadProgress

    /**
     * Save book to library.
     */
    suspend fun saveBookToLibrary(bookId: String)

    /**
     * Get all saved book.
     */
    fun getAllSavedBook(): Flow<List<BookModelTemp>>

    /**
     * Delete saved book.
     */
    suspend fun deleteSavedBook(bookId: String)

    /**
     * Get saved book by id.
     */
    fun getSavedBookById(id: String): Flow<BookModelTemp?>

    fun getBookCache(bookId: String): Flow<BookModelTemp?>
}
