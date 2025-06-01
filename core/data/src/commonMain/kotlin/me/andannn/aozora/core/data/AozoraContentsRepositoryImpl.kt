/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.andannn.aozora.core.data.mapper.toModel
import me.andannn.aozora.core.database.dao.SavedBookDao
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.BookColumnItem
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

    override fun getBookCard(
        cardId: String,
        authorId: String,
    ): Flow<AozoraBookCard?> {
        return savedBookDao.getBookByBookIdAndAuthorId(bookId = cardId, authorId = authorId)
            .map {
                it?.toModel()
            }
    }
}