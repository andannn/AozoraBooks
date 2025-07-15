/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.andannn.aozora.core.data.mapper.toModel
import me.andannn.aozora.core.data.ndc.NdcDataHodler
import me.andannn.aozora.core.database.dao.BookLibraryDao
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.model.AuthorWithBooks
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NdcData
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.core.service.AozoraService

private const val TAG = "AozoraContentsRepository"

internal class AozoraContentsRepositoryImpl(
    private val aozoraService: AozoraService,
    private val dao: BookLibraryDao,
) : AozoraContentsRepository {
    private val ndcDataHodler = NdcDataHodler()

    override fun getBookListPagingFlow(kana: String): Flow<PagingData<AozoraBookCard>> =
        Pager(
            config = PagingConfig(pageSize = LOAD_SIZE),
            pagingSourceFactory = { dao.kanaPagingSource(kana) },
        ).flow.map { pagingData ->
            pagingData.map { it.toModel() }
        }

    override fun getAuthorsPagingFlow(kanaLineItem: KanaLineItem): Flow<PagingData<AuthorData>> =
        Pager(
            config = PagingConfig(pageSize = LOAD_SIZE),
            pagingSourceFactory = { dao.authorPagingSource(kanaLineItem.kanaList) },
        ).flow.map { pagingData ->
            pagingData.map { it.toModel() }
        }

    override fun getBookCard(
        cardId: String,
        authorId: String,
    ): Flow<AozoraBookCard?> =
        combine(
            getBookCardAuthorDataListOrNullFlow(cardId, authorId),
            dao
                .getBookByBookIdAndAuthorId(
                    bookId = cardId,
                    authorId = authorId,
                ).filterNotNull()
                .map { it.toModel() },
        ) { authorData, bookModel ->
            if (authorData != null) {
                bookModel.copy(authorDataList = authorData)
            } else {
                bookModel
            }
        }

    override fun getAuthorDataWithBooks(authorId: String): Flow<AuthorWithBooks?> =
        dao.getAuthorWithBooks(authorId).map {
            it?.toModel()
        }

    override suspend fun searchBooks(query: String) =
        dao.searchBook(query).map {
            it.toModel()
        }

    override suspend fun searchAuthors(query: String) =
        dao.searchAuthor(query).map {
            it.toModel()
        }

    override suspend fun getChildrenOfNDC(ndcClassification: NDCClassification): List<NdcData> =
        ndcDataHodler.getChildrenOfNDC(ndcClassification)

    override suspend fun getNDCDetails(ndc: NDCClassification): NdcData? = ndcDataHodler.getNdcDataByClassification(ndc)

    private fun getBookCardAuthorDataListOrNullFlow(
        cardId: String,
        authorId: String,
    ) = flow {
        val authorData =
            try {
                aozoraService.getBookCardAuthorDataList(
                    cardId = cardId,
                    groupId = authorId,
                )
            } catch (e: Exception) {
                Napier.e(tag = TAG) { "getBookCardAuthorDataListOrNullFlow error $e" }
                null
            }
        emit(authorData)
    }

    companion object {
        private const val LOAD_SIZE = 50
    }
}
