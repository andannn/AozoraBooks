/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import me.andannn.aozora.core.database.dao.READ_PROGRESS_DONE
import me.andannn.aozora.core.database.dao.SavedBookDao
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookProgressEntity
import me.andannn.aozora.core.database.entity.SavedBookEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DatabaseTest {
    private val database: AozoraDataBase =
        Room
            .inMemoryDatabaseBuilder<AozoraDataBase>()
            .setDriver(BundledSQLiteDriver())
            .build()

    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private val savedBookDao: SavedBookDao
        get() = database.savedBookDao()

    @Test
    fun getSavedBooksTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "1",
                    createdDate = 1,
                ),
            )
            assertEquals(1, savedBookDao.getNotCompletedBooksByDesc().first().size)

            savedBookDao.deleteSavedBook("1")
            assertEquals(0, savedBookDao.getNotCompletedBooksByDesc().first().size)
        }

    @Test
    fun getSavedBookByIdTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "1",
                    createdDate = 1,
                ),
            )
            assertEquals(
                "1",
                savedBookDao.getSavedBookById("1").first()?.bookId,
            )
            savedBookDao.upsertBookList(bookEntities)
            assertEquals(
                "1",
                savedBookDao.getSavedBookById("1").first()?.bookId,
            )
            savedBookDao.deleteSavedBook("1")
            assertTrue {
                savedBookDao.getSavedBookById("1").first() == null
            }
        }

    @Test
    fun getSavedBookByDataDescTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "2",
                    createdDate = 2,
                ),
            )
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "1",
                    createdDate = 1,
                ),
            )
            val savedBooks = savedBookDao.getNotCompletedBooksByDesc().first()
            println(savedBooks)
            assertTrue {
                savedBooks[0].book.bookId == "2"
            }
            assertTrue {
                savedBooks[1].book.bookId == "1"
            }
        }

    @Test
    fun insertSavedBookTest() =
        testScope.runTest {
            val progressEntity =
                BookProgressEntity(
                    bookId = "1",
                    progressBlockIndex = 1,
                    updateEpochMillisecond = 1,
                )
            savedBookDao.updateProgressOfBook(progressEntity)
            assertTrue {
                savedBookDao.getProgressOfBook("1") != null
            }

            savedBookDao.updateProgressOfBook(progressEntity.copy(updateEpochMillisecond = 2))
            assertTrue {
                savedBookDao.getProgressOfBook("1")?.updateEpochMillisecond == 2L
            }
        }

    @Test
    fun insertAndGetProgressTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "1",
                    createdDate = 1,
                ),
            )

            // saved book is not empty but progress is empty.
            assertTrue {
                savedBookDao.getNotCompletedBooksByDesc().first().size == 1
            }
            assertTrue {
                savedBookDao.getNotCompletedBooksByDesc().first()[0].progress == null
            }

            savedBookDao.updateProgressOfBook(
                BookProgressEntity(
                    bookId = "1",
                    progressBlockIndex = 1,
                    updateEpochMillisecond = 1,
                ),
            )
            // get Book with progress.
            assertTrue {
                savedBookDao
                    .getNotCompletedBooksByDesc()
                    .first()[0]
                    .progress
                    ?.progressBlockIndex == 1
            }
        }

    @Test
    fun setAndGetReadingProgressTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "1",
                    createdDate = 1,
                ),
            )
            assertEquals(1, savedBookDao.getNotCompletedBooksByDesc().first().size)

            savedBookDao.updateProgressOfBook(
                BookProgressEntity(
                    bookId = "1",
                    progressBlockIndex = READ_PROGRESS_DONE,
                    updateEpochMillisecond = 1,
                    markCompleted = true,
                ),
            )

            assertEquals(0, savedBookDao.getNotCompletedBooksByDesc().first().size)

            assertTrue {
                savedBookDao.getCompleteBooksByDesc().first().isNotEmpty()
            }
        }

    @Test
    fun getBookEntityById() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            val result =
                savedBookDao.getBookByBookIdAndAuthorId(
                    bookId = "056078",
                    authorId = "001257",
                )
            assertEquals("056078", result.first()?.bookId)
        }
}

private val bookEntities =
    listOf(
        BookEntity(
            bookId = "1",
            title = "title",
            titleKana = "titleKana",
            titleSortKana = null,
            subtitle = null,
            subtitleKana = null,
            originalTitle = null,
            firstAppearance = null,
            categoryNo = null,
            orthography = null,
            workCopyrightFlag = null,
            publishDate = null,
            lastUpdateDate = null,
            cardUrl = "authorUrl",
            authorId = "1",
            authorLastName = "author",
            authorFirstName = "",
            authorLastNameKana = null,
            authorFirstNameKana = null,
            authorLastNameSortKana = null,
            authorFirstNameSortKana = null,
            authorLastNameRomaji = null,
            authorFirstNameRomaji = null,
            authorRoleFlag = null,
            authorBirth = null,
            authorDeath = null,
            authorCopyrightFlag = null,
            sourceBook1 = null,
            sourcePublisher1 = null,
            sourcePubYear1 = null,
            inputEdition1 = null,
            proofEdition1 = null,
            parentSourceBook1 = null,
            parentSourcePublisher1 = null,
            parentSourcePubYear1 = null,
            sourceBook2 = null,
            sourcePublisher2 = null,
            sourcePubYear2 = null,
            inputEdition2 = null,
            proofEdition2 = null,
            parentSourceBook2 = null,
            parentSourcePublisher2 = null,
            parentSourcePubYear2 = null,
            inputBy = null,
            proofBy = null,
            textFileUrl = "zipUrl",
            textFileLastUpdate = null,
            textFileEncoding = null,
            textFileCharset = null,
            textFileRevision = null,
            htmlFileUrl = "htmlUrl",
            htmlFileLastUpdate = null,
            htmlFileEncoding = null,
            htmlFileCharset = null,
            htmlFileRevision = null,
        ),
        BookEntity(
            bookId = "2",
            title = "title2",
            titleKana = "titleKana2",
            titleSortKana = null,
            subtitle = null,
            subtitleKana = null,
            originalTitle = null,
            firstAppearance = null,
            categoryNo = null,
            orthography = null,
            workCopyrightFlag = null,
            publishDate = null,
            lastUpdateDate = null,
            cardUrl = "authorUrl2",
            authorId = "2",
            authorLastName = "author2",
            authorFirstName = "",
            authorLastNameKana = null,
            authorFirstNameKana = null,
            authorLastNameSortKana = null,
            authorFirstNameSortKana = null,
            authorLastNameRomaji = null,
            authorFirstNameRomaji = null,
            authorRoleFlag = null,
            authorBirth = null,
            authorDeath = null,
            authorCopyrightFlag = null,
            sourceBook1 = null,
            sourcePublisher1 = null,
            sourcePubYear1 = null,
            inputEdition1 = null,
            proofEdition1 = null,
            parentSourceBook1 = null,
            parentSourcePublisher1 = null,
            parentSourcePubYear1 = null,
            sourceBook2 = null,
            sourcePublisher2 = null,
            sourcePubYear2 = null,
            inputEdition2 = null,
            proofEdition2 = null,
            parentSourceBook2 = null,
            parentSourcePublisher2 = null,
            parentSourcePubYear2 = null,
            inputBy = null,
            proofBy = null,
            textFileUrl = "zipUrl2",
            textFileLastUpdate = null,
            textFileEncoding = null,
            textFileCharset = null,
            textFileRevision = null,
            htmlFileUrl = "htmlUrl2",
            htmlFileLastUpdate = null,
            htmlFileEncoding = null,
            htmlFileCharset = null,
            htmlFileRevision = null,
        ),
        BookEntity(
            bookId = "056078",
            title = "title3",
            titleKana = "titleKana3",
            titleSortKana = null,
            subtitle = null,
            subtitleKana = null,
            originalTitle = null,
            firstAppearance = null,
            categoryNo = null,
            orthography = null,
            workCopyrightFlag = null,
            publishDate = null,
            lastUpdateDate = null,
            cardUrl = "authorUrl3",
            authorId = "001257",
            authorLastName = "author3",
            authorFirstName = "",
            authorLastNameKana = null,
            authorFirstNameKana = null,
            authorLastNameSortKana = null,
            authorFirstNameSortKana = null,
            authorLastNameRomaji = null,
            authorFirstNameRomaji = null,
            authorRoleFlag = null,
            authorBirth = null,
            authorDeath = null,
            authorCopyrightFlag = null,
            sourceBook1 = null,
            sourcePublisher1 = null,
            sourcePubYear1 = null,
            inputEdition1 = null,
            proofEdition1 = null,
            parentSourceBook1 = null,
            parentSourcePublisher1 = null,
            parentSourcePubYear1 = null,
            sourceBook2 = null,
            sourcePublisher2 = null,
            sourcePubYear2 = null,
            inputEdition2 = null,
            proofEdition2 = null,
            parentSourceBook2 = null,
            parentSourcePublisher2 = null,
            parentSourcePubYear2 = null,
            inputBy = null,
            proofBy = null,
            textFileUrl = "zipUrl3",
            textFileLastUpdate = null,
            textFileEncoding = null,
            textFileCharset = null,
            textFileRevision = null,
            htmlFileUrl = "htmlUrl3",
            htmlFileLastUpdate = null,
            htmlFileEncoding = null,
            htmlFileCharset = null,
            htmlFileRevision = null,
        ),
    )
