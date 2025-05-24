/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.domain.model.BookColumnItem
import me.andannn.aozora.core.service.AozoraService

private const val TAG = "BookListPagingSource"

internal class BookListPagingSource(
    private val kana: String,
    private val aozoraService: AozoraService,
) : PagingSource<Int, BookColumnItem>() {
    private val pageCount: Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookColumnItem> {
        return try {
            val pageCount = pageCount ?: aozoraService.getPageCountOfKana(kana)
            Napier.d(tag = TAG) { "load page $params, pageCount: $pageCount" }
            val requestPage = params.key ?: 1

            if (requestPage > pageCount) {
                return LoadResult.Error(
                    Exception("No more pages"),
                )
            }

            val response = fetchData(requestPage)
            LoadResult.Page(
                data = response,
                prevKey = if (requestPage == 1) null else requestPage - 1,
                nextKey = if (requestPage == pageCount) null else requestPage + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookColumnItem>): Int? = state.anchorPosition

    private suspend fun fetchData(page: Int): List<BookColumnItem> = aozoraService.getBookListOfKanaByPage(kana, page)
}
