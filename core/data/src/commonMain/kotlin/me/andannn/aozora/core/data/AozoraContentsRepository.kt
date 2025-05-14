/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.BookColumnItem

interface AozoraContentsRepository {
    fun getBookListPagingFlow(kana: String): Flow<PagingData<BookColumnItem>>

    suspend fun getBookCard(
        cardId: String,
        groupId: String,
    ): LoadResult<AozoraBookCard>
}
