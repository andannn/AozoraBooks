/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import me.andannn.aozora.core.database.dao.BookLibraryDao
import me.andannn.aozora.core.database.dao.READ_PROGRESS_DONE
import me.andannn.aozora.core.database.entity.BookProgressEntity
import me.andannn.aozora.core.database.entity.SavedBookEntity
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractDatabaseTest {
    private lateinit var database: AozoraDataBase

    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private val savedBookDao: BookLibraryDao
        get() = database.savedBookDao()

    abstract val inMemoryDatabaseBuilder: RoomDatabase.Builder<AozoraDataBase>

    @BeforeTest
    fun setup() {
        database = inMemoryDatabaseBuilder.build()
    }

    @Test
    fun getSavedBooksTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "12353",
                    authorId = "1",
                    createdDate = 1,
                ),
            )
            assertEquals(1, savedBookDao.getNotCompletedBooksByDesc().first().size)

            savedBookDao.deleteSavedBook("12353", "1")
            assertEquals(0, savedBookDao.getNotCompletedBooksByDesc().first().size)
        }

    @Test
    fun getSavedBookByIdTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "12353",
                    authorId = "1",
                    createdDate = 1,
                ),
            )
            assertEquals(
                "12353",
                savedBookDao.getSavedBookById("12353", "1").first()?.bookId,
            )
            savedBookDao.upsertBookList(bookEntities)
            assertEquals(
                "12353",
                savedBookDao.getSavedBookById("12353", "1").first()?.bookId,
            )
            savedBookDao.deleteSavedBook("12353", "1")
            assertTrue {
                savedBookDao.getSavedBookById("12353", "1").first() == null
            }
        }

    @Test
    fun getSavedBookByDataDescTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "056078",
                    authorId = "001257",
                    createdDate = 2,
                ),
            )
            savedBookDao.upsertSavedBook(
                SavedBookEntity(
                    bookId = "212353",
                    authorId = "2",
                    createdDate = 1,
                ),
            )
            val savedBooks = savedBookDao.getNotCompletedBooksByDesc().first()
            println(savedBooks)
            assertTrue {
                savedBooks[0].book.bookId == "056078"
            }
            assertTrue {
                savedBooks[1].book.bookId == "212353"
            }
        }

    @Test
    fun insertSavedBookTest() =
        testScope.runTest {
            val progressEntity =
                BookProgressEntity(
                    bookId = "1",
                    authorId = "2",
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
                    bookId = "12353",
                    authorId = "1",
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
                    bookId = "12353",
                    authorId = "1",
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
                    bookId = "12353",
                    authorId = "1",
                    createdDate = 1,
                ),
            )
            assertEquals(1, savedBookDao.getNotCompletedBooksByDesc().first().size)

            savedBookDao.updateProgressOfBook(
                BookProgressEntity(
                    bookId = "12353",
                    authorId = "1",
                    progressBlockIndex = READ_PROGRESS_DONE,
                    updateEpochMillisecond = 1,
                    markCompleted = true,
                ),
            )

            assertEquals(0, savedBookDao.getNotCompletedBooksByDesc().first().size)

            assertEquals(
                1,
                savedBookDao.getCompleteBooksByDesc().first().size,
            )
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

    @Test
    fun getAuthorWithBooks() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertAuthorList(authorList)
            val result = savedBookDao.getAuthorWithBooks("001257")
            assertEquals("author3", result.first()?.author?.lastName)
            assertEquals(1, result.first()?.books?.size)
            assertEquals(
                "056078",
                result
                    .first()
                    ?.books
                    ?.first()
                    ?.bookId,
            )
        }

    @Test
    fun searchBookByKeywordTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            val result = savedBookDao.searchBook("titleK*")
            println(result)
        }

    @Test
    fun searchAuthorByKeywordTest() =
        testScope.runTest {
            savedBookDao.upsertAuthorList(authorList)
            val result = savedBookDao.searchAuthor("南部*")
            println(result)
        }

    @Test
    fun getBooksOfNDCClassificationTest() =
        testScope.runTest {
            savedBookDao.upsertBookList(bookEntities)
            savedBookDao.upsertBookIdWithBookCategoryList(bookIdWithBookCategoryList)
            val result1 = savedBookDao.getBooksWithClassification(1, 3, 3)
            assertEquals(2, result1.size)
            assertEquals("212353", result1[0].bookId)
            assertEquals("056078", result1[1].bookId)
        }
}
