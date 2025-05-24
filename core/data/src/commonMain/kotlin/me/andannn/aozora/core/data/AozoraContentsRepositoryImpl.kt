/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import me.andannn.aozora.core.database.dao.SavedBookDao
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.BookColumnItem
import me.andannn.aozora.core.domain.model.LoadResult
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.core.service.AozoraService

private const val TAG = "AozoraContentsRepository"

internal class AozoraContentsRepositoryImpl(
    private val aozoraService: AozoraService,
    private val savedBookDao: SavedBookDao,
) : AozoraContentsRepository {
    override fun getBookListPagingFlow(kana: String): Flow<PagingData<BookColumnItem>> =
        Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { BookListPagingSource(kana, aozoraService) },
        ).flow

    override suspend fun getBookCard(
        cardId: String,
        groupId: String,
    ): LoadResult<AozoraBookCard> {
        try {
            val bookCard = aozoraService.getBookCard(groupId = groupId, cardId = cardId)
            savedBookDao.upsertBookList(listOf(bookCard.mapToEntity()))
            return LoadResult.Success(bookCard)
        } catch (e: Exception) {
            Napier.e { "getBookCard: $e" }
            return LoadResult.Error(e)
        }
    }
}

private fun AozoraBookCard.mapToEntity() =
    BookEntity(
        bookId = id,
        groupId = groupId,
        title = title,
        author = author,
        titleKana = titleKana,
        authorUrl = authorUrl,
        zipUrl = zipUrl,
        htmlUrl = htmlUrl,
        savedDateInEpochMillisecond = Clock.System.now().toEpochMilliseconds(),
    )
