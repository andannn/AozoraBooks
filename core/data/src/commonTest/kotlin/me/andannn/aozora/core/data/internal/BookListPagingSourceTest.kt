package me.andannn.aozora.core.data.internal

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.BookColumnItem
import me.andannn.aozora.core.data.common.TitleItem
import me.andannn.aozora.core.service.AozoraService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BookListPagingSourceTest {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    @Test
    fun bookListPagingSourceTest() =
        testScope.runTest {
            val pagingSource = BookListPagingSource("a", dummyService)

            val result =
                pagingSource.load(
                    PagingSource.LoadParams.Append(
                        key = 2,
                        loadSize = 1,
                        placeholdersEnabled = false,
                    ),
                )

            assertIs<LoadResult.Page<Int, BookColumnItem>>(result)
            assertEquals(null, result.nextKey)

            val result2 =
                pagingSource.load(
                    PagingSource.LoadParams.Append(
                        key = 3,
                        loadSize = 1,
                        placeholdersEnabled = false,
                    ),
                )
            assertIs<LoadResult.Error<Int, BookColumnItem>>(result2)
        }
}

private val dummyService =
    object : AozoraService {
        override suspend fun getPageCountOfKana(kana: String): Int = 2

        override suspend fun getBookListOfKanaByPage(
            kana: String,
            page: Int,
        ): List<BookColumnItem> =
            buildList {
                repeat(20) {
                    add(
                        BookColumnItem(
                            index = "page $page index $it",
                            title =
                                TitleItem(
                                    title = "title",
                                    subTitle = "subtitle",
                                    link = "link",
                                ),
                            characterCategory = "",
                            author = "",
                            translator = null
                        ),
                    )
                }
            }

        override suspend fun getBookCard(groupId: String, cardId: String): AozoraBookCard {
            error("")
        }
    }
