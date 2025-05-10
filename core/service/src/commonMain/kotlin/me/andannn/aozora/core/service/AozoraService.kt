/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.service

import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.BookColumnItem

interface AozoraService {
    suspend fun getPageCountOfKana(kana: String): Int

    suspend fun getBookListOfKanaByPage(
        kana: String,
        page: Int,
    ): List<BookColumnItem>

    suspend fun getBookCard(
        groupId: String,
        cardId: String,
    ): AozoraBookCard
}
