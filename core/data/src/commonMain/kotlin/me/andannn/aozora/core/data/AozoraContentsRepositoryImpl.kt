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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.andannn.aozora.core.data.mapper.toModel
import me.andannn.aozora.core.database.dao.SavedBookDao
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
    ): Flow<AozoraBookCard?> =
        flow {
            val authorData =
                try {
                    aozoraService.getBookCardAuthorDataList(
                        groupId = authorId,
                        cardId = cardId,
                    )
                } catch (e: Exception) {
                    Napier.e(tag = TAG) { "getBookCard error $e" }
                    null
                }
            val dbBookModel =
                savedBookDao
                    .getBookByBookIdAndAuthorId(bookId = cardId, authorId = authorId)
                    .filterNotNull()
                    .map { entity ->
                        if (authorData != null) {
                            entity.toModel().copy(
                                authorDataList = authorData,
                            )
                        } else {
                            entity.toModel()
                        }
                    }

            emitAll(dbBookModel)
        }
}
