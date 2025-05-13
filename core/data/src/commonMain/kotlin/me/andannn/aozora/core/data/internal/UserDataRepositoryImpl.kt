/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.BookModelTemp
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.data.common.TopMargin
import me.andannn.aozora.core.database.dao.SavedBookDao
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookProgressEntity
import me.andannn.aozora.core.database.entity.SavedBookEntity
import me.andannn.aozora.core.datastore.UserSettingPreferences

internal class UserDataRepositoryImpl(
    private val preferences: UserSettingPreferences,
    private val dao: SavedBookDao,
) : UserDataRepository {
    override fun getFontSizeLevel(): Flow<FontSizeLevel> =
        preferences.userData.map {
            it.fontSizeLevel
        }

    override fun getFontFontType(): Flow<FontType> =
        preferences.userData.map {
            it.fontType
        }

    override fun getTopMargin(): Flow<TopMargin> =
        preferences.userData.map {
            it.topMargin
        }

    override fun getLineSpacing(): Flow<LineSpacing> =
        preferences.userData.map {
            it.lineSpacing
        }

    override fun getReaderTheme(): Flow<ReaderTheme> =
        preferences.userData.map {
            it.readerTheme
        }

    override suspend fun setFontSizeLevel(fontSizeLevel: FontSizeLevel) {
        preferences.setFontSizeLevel(fontSizeLevel)
    }

    override suspend fun setFontType(fontType: FontType) {
        preferences.setFontType(fontType)
    }

    override suspend fun setTopMargin(topMargin: TopMargin) {
        preferences.setTopMargin(topMargin)
    }

    override suspend fun setLineSpacing(lineSpacing: LineSpacing) {
        preferences.setLineSpacing(lineSpacing)
    }

    override suspend fun setReaderTheme(readerTheme: ReaderTheme) {
        preferences.setReaderTheme(readerTheme)
    }

    override suspend fun setProgressOfBook(
        bookCardId: String,
        blockIndex: Int,
    ) {
        dao.updateProgressOfBook(
            BookProgressEntity(
                bookId = bookCardId,
                progressBlockIndex = blockIndex,
                updateEpochMillisecond = Clock.System.now().toEpochMilliseconds(),
            ),
        )
    }

    override fun getProgressFlow(bookCardId: String): Flow<Int?> = dao.getProgressOfBookFlow(bookCardId).map { it.progressBlockIndex }

    override suspend fun getProgress(bookCardId: String): Int? = dao.getProgressOfBook(bookCardId)?.progressBlockIndex

    override suspend fun saveBookToLibrary(bookId: String) {
        dao.upsertSavedBook(
            SavedBookEntity(
                bookId = bookId,
                createdDate = Clock.System.now().toEpochMilliseconds(),
            ),
        )
    }

    override fun getAllSavedBook(): Flow<List<BookModelTemp>> =
        dao.getSavedBooks().map {
            it.map(BookEntity::toModel)
        }

    override suspend fun deleteSavedBook(bookId: String) {
        dao.deleteSavedBook(bookId)
    }

    override fun getSavedBookById(id: String): Flow<BookModelTemp?> =
        dao.getSavedBookById(id).map {
            it?.toModel()
        }

    override fun getBookCache(bookId: String) =
        dao.getBookById(bookId).map {
            it?.toModel()
        }
}

private fun BookEntity.toModel() =
    BookModelTemp(
        id = bookId,
        groupId = groupId,
        title = title,
        titleKana = titleKana,
        authorName = author,
        zipUrl = zipUrl,
        htmlUrl = htmlUrl,
    )
