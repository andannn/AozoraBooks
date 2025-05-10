package me.andannn.aozora.core.database

import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import me.andannn.aozora.core.database.dao.SavedBookDao
import me.andannn.aozora.core.database.entity.SavedBookEntity
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal expect fun inMemoryDatabaseBuilder(): RoomDatabase.Builder<AozoraDataBase>

class DatabaseTest {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private lateinit var database: AozoraDataBase
    private val savedBookDao: SavedBookDao
        get() = database.savedBookDao()

    @BeforeTest
    fun setup() {
        database =
            inMemoryDatabaseBuilder()
                .setUpDatabase()
                .build()
    }

    @Test
    fun getSavedBooksTest() =
        testScope.runTest {
            savedBookDao.insertSavedBook(
                bookEntities,
            )

            assertEquals(2, savedBookDao.getSavedBooks().first().size)
        }
}

private val bookEntities =
    listOf<SavedBookEntity>(
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
