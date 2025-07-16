/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.model.AuthorWithBooks
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NdcData

interface AozoraContentsRepository {
    fun getBookListPagingFlow(kana: String): Flow<PagingData<AozoraBookCard>>

    fun getAuthorsPagingFlow(kanaLineItem: KanaLineItem): Flow<PagingData<AuthorData>>

    fun getBookEntitiesOfNdcClassificationFlow(ndcClassification: NDCClassification): Flow<PagingData<AozoraBookCard>>

    fun getBookCard(
        cardId: String,
        authorId: String,
    ): Flow<AozoraBookCard?>

    fun getAuthorDataWithBooks(authorId: String): Flow<AuthorWithBooks?>

    suspend fun searchBooks(query: String): List<AozoraBookCard>

    suspend fun searchAuthors(query: String): List<AuthorData>

    /**
     * Returns children of NDC classifications.
     *
     * @param ndcClassification The NDC classification to get children for.
     * @return A list of [NdcData] representing the children of the given NDC classification.
     */
    suspend fun getChildrenOfNDC(ndcClassification: NDCClassification): List<NdcData>

    /**
     * Retrieves the details of a specific NDC classification.
     *
     * @param ndc The NDC classification to get details for.
     * @return An [NdcData] object containing the details of the specified NDC classification.
     */
    suspend fun getNDCDetails(ndc: NDCClassification): NdcData?
}
