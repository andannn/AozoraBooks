/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.BookColumnItem
import me.andannn.aozora.core.domain.model.LoadResult

interface AozoraContentsRepository {
    fun getBookListPagingFlow(kana: String): Flow<PagingData<BookColumnItem>>

    fun getBookCard(
        cardId: String,
        authorId: String,
    ): Flow<AozoraBookCard?>
}
