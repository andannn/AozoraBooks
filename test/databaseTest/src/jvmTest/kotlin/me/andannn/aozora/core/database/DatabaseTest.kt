package me.andannn.aozora.core.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import me.andannn.aozora.core.database.dao.SavedBookDao
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
            savedBookDao.insertSavedBook(bookEntities)
            assertEquals(2, savedBookDao.getSavedBooks().first().size)

            savedBookDao.insertSavedBook(bookEntities)
            assertEquals(2, savedBookDao.getSavedBooks().first().size)
        }

    @Test
    fun deleteSavedBookTest() =
        testScope.runTest {
            savedBookDao.insertSavedBook(
                bookEntities,
            )
            savedBookDao.deleteSavedBook("1")
            assertEquals(1, savedBookDao.getSavedBooks().first().size)
        }

    @Test
    fun getSavedBookByIdTest() =
        testScope.runTest {
            savedBookDao.insertSavedBook(
                bookEntities,
            )
            assertTrue {
                savedBookDao.getSavedBookById("1").first() != null
            }
            savedBookDao.deleteSavedBook("1")
            assertTrue {
                savedBookDao.getSavedBookById("1").first() == null
            }
        }
}

private val bookEntities =
    listOf(
        SavedBookEntity(
            bookId = "1",
            groupId = "1",
            title = "title",
            titleKana = "titleKana",
            author = "author",
            authorUrl = "authorUrl",
            zipUrl = "zipUrl",
            htmlUrl = "htmlUrl",
            savedDate = 1L,
        ),
        SavedBookEntity(
            bookId = "2",
            groupId = "2",
            title = "title2",
            titleKana = "titleKana2",
            author = "author2",
            authorUrl = "authorUrl2",
            zipUrl = "zipUrl2",
            htmlUrl = "htmlUrl2",
            savedDate = 2L,
        ),
    )
