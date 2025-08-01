/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import me.andannn.aozora.core.data.mapper.toModel
import me.andannn.aozora.core.database.dao.BookLibraryDao
import me.andannn.aozora.core.database.embedded.BookEntityWithProgress
import me.andannn.aozora.core.database.entity.BookProgressEntity
import me.andannn.aozora.core.database.entity.SavedBookEntity
import me.andannn.aozora.core.datastore.UserSettingPreferences
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.BookWithProgress
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.LineSpacing
import me.andannn.aozora.core.domain.model.READ_PROGRESS_DONE
import me.andannn.aozora.core.domain.model.READ_PROGRESS_NONE
import me.andannn.aozora.core.domain.model.ReadProgress
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.core.domain.model.TopMargin
import me.andannn.aozora.core.domain.repository.UserDataRepository

internal class UserDataRepositoryImpl(
    private val preferences: UserSettingPreferences,
    private val dao: BookLibraryDao,
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
        authorId: String,
        readProgress: ReadProgress,
    ) {
        val progress = dao.getProgressOfBook(bookCardId)
        if (progress == null) {
            dao.updateProgressOfBook(
                BookProgressEntity(
                    bookId = bookCardId,
                    authorId = authorId,
                    progressBlockIndex = readProgress.toDataBaseValue(),
                    totalBlockCount = (readProgress as? ReadProgress.Reading)?.totalBlockCount,
                    updateEpochMillisecond = Clock.System.now().toEpochMilliseconds(),
                ),
            )
        } else {
            dao.updateProgressOfBook(
                progress.copy(
                    progressBlockIndex = readProgress.toDataBaseValue(),
                    totalBlockCount = (readProgress as? ReadProgress.Reading)?.totalBlockCount,
                    updateEpochMillisecond = Clock.System.now().toEpochMilliseconds(),
                ),
            )
        }
    }

    override suspend fun markBookAsCompleted(
        bookCardId: String,
        authorId: String,
    ) {
        val progress = dao.getProgressOfBook(bookCardId)
        if (progress == null) {
            dao.updateProgressOfBook(
                BookProgressEntity(
                    bookId = bookCardId,
                    authorId = authorId,
                    progressBlockIndex = READ_PROGRESS_NONE,
                    updateEpochMillisecond = Clock.System.now().toEpochMilliseconds(),
                    markCompleted = true,
                ),
            )
        } else {
            dao.updateProgressOfBook(
                progress.copy(
                    updateEpochMillisecond = Clock.System.now().toEpochMilliseconds(),
                    markCompleted = true,
                ),
            )
        }
    }

    override suspend fun markBookAsNotCompleted(bookCardId: String) {
        val progress = dao.getProgressOfBook(bookCardId) ?: return
        dao.updateProgressOfBook(
            progress.copy(
                updateEpochMillisecond = Clock.System.now().toEpochMilliseconds(),
                markCompleted = false,
            ),
        )
    }

    override suspend fun getProgress(bookCardId: String): ReadProgress = dao.getProgressOfBook(bookCardId).toReadProgress()

    override fun getProgressFlow(bookCardId: String): Flow<ReadProgress> =
        dao.getProgressOfBookFlow(bookCardId).map {
            it.toReadProgress()
        }

    override fun isUserMarkCompletedFlow(bookCardId: String): Flow<Boolean> =
        dao.getProgressOfBookFlow(bookCardId).map {
            it?.markCompleted == true
        }

    override suspend fun saveBookToLibrary(
        bookId: String,
        authorId: String,
    ) {
        dao.upsertSavedBook(
            SavedBookEntity(
                bookId = bookId,
                authorId = authorId,
                createdDate = Clock.System.now().toEpochMilliseconds(),
            ),
        )
    }

    override fun getAllNotCompletedBooks(): Flow<List<BookWithProgress>> =
        dao.getNotCompletedBooksByDesc().map {
            it.map(BookEntityWithProgress::toModel)
        }

    override fun getAllCompletedBooks(): Flow<List<BookWithProgress>> =
        dao.getCompleteBooksByDesc().map {
            it.map(BookEntityWithProgress::toModel)
        }

    override suspend fun deleteSavedBook(
        bookId: String,
        authorId: String,
    ) {
        dao.deleteSavedBook(bookId, authorId)
    }

    override fun getSavedBookById(
        bookId: String,
        authorId: String,
    ): Flow<AozoraBookCard?> =
        dao.getSavedBookById(bookId, authorId).map {
            it?.toModel()
        }

    override fun getBookCache(
        bookId: String,
        authorId: String,
    ) = dao.getBookByBookIdAndAuthorId(bookId, authorId).map {
        it?.toModel()
    }

    override suspend fun isNdcTableMigrated(): Boolean = preferences.userData.first().ndcTableMigrated
}

private fun BookEntityWithProgress.toModel() =
    BookWithProgress(
        book = book.toModel(),
        progress = progress.toReadProgress(),
        isUserMarkCompleted = progress?.markCompleted == true,
    )

private fun ReadProgress.toDataBaseValue(): Int =
    when (this) {
        ReadProgress.Done -> READ_PROGRESS_DONE
        ReadProgress.None -> READ_PROGRESS_NONE
        is ReadProgress.Reading -> blockIndex
    }

private fun BookProgressEntity?.toReadProgress(): ReadProgress =
    when (val blockIndex = this?.progressBlockIndex) {
        null, READ_PROGRESS_NONE -> {
            ReadProgress.None
        }

        READ_PROGRESS_DONE -> {
            ReadProgress.Done
        }

        else -> {
            ReadProgress.Reading(
                blockIndex = blockIndex,
                totalBlockCount = this?.totalBlockCount,
            )
        }
    }
