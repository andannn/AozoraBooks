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
                savedBookDao.getNotCompletedBooksByDesc().first()[0].progress?.progressBlockIndex == 1
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
            assertTrue {
                savedBookDao.getNotCompletedBooksByDesc().first().size == 1
            }

            savedBookDao.updateProgressOfBook(
                BookProgressEntity(
                    bookId = "1",
                    progressBlockIndex = READ_PROGRESS_DONE,
                    updateEpochMillisecond = 1,
                ),
            )

            assertTrue {
                savedBookDao.getNotCompletedBooksByDesc().first().isEmpty()
            }

            assertTrue {
                savedBookDao.getCompleteBooksByDesc().first().isNotEmpty()
            }
        }
}

private val bookEntities =
    listOf(
        BookEntity(
            bookId = "1",
            groupId = "1",
            title = "title",
            titleKana = "titleKana",
            author = "author",
            authorUrl = "authorUrl",
            zipUrl = "zipUrl",
            htmlUrl = "htmlUrl",
            savedDateInEpochMillisecond = 1L,
        ),
        BookEntity(
            bookId = "2",
            groupId = "2",
            title = "title2",
            titleKana = "titleKana2",
            author = "author2",
            authorUrl = "authorUrl2",
            zipUrl = "zipUrl2",
            htmlUrl = "htmlUrl2",
            savedDateInEpochMillisecond = 2L,
        ),
        BookEntity(
            bookId = "3",
            groupId = "3",
            title = "title3",
            titleKana = "titleKana3",
            author = "author3",
            authorUrl = "authorUrl3",
            zipUrl = "zipUrl3",
            htmlUrl = "htmlUrl3",
            savedDateInEpochMillisecond = 3L,
        ),
    )
