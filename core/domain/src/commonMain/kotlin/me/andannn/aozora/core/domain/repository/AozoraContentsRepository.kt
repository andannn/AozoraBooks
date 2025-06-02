/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.domain.model.AozoraBookCard

interface AozoraContentsRepository {
    fun getBookListPagingFlow(kana: String): Flow<PagingData<AozoraBookCard>>

    fun getBookCard(
        cardId: String,
        authorId: String,
    ): Flow<AozoraBookCard?>
}
