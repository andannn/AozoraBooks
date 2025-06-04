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

interface AozoraContentsRepository {
    fun getBookListPagingFlow(kana: String): Flow<PagingData<AozoraBookCard>>

    fun getAuthorsPagingFlow(kanaLineItem: KanaLineItem): Flow<PagingData<AuthorData>>

    fun getBookCard(
        cardId: String,
        authorId: String,
    ): Flow<AozoraBookCard?>

    fun getAuthorDataWithBooks(authorId: String): Flow<AuthorWithBooks?>

    suspend fun searchBooks(query: String): List<AozoraBookCard>

    suspend fun searchAuthors(query: String): List<AuthorData>
}
