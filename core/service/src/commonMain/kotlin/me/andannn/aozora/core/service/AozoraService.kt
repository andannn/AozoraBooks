/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.service

import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.model.BookColumnItem

interface AozoraService {
    suspend fun getPageCountOfKana(kana: String): Int

    suspend fun getBookListOfKanaByPage(
        kana: String,
        page: Int,
    ): List<BookColumnItem>

    suspend fun getBookCardAuthorDataList(
        groupId: String,
        cardId: String,
    ): List<AuthorData>
}
