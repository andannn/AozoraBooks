/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

data class AuthorData(
    val authorId: String,
    val authorName: String,
    val authorNameKana: String?,
    val authorNameRomaji: String?,
    val birth: String? = null,
    val death: String? = null,
    val category: String? = null,
    val authorUrl: String? = null,
    val description: String? = null,
    val descriptionWikiUrl: String? = null,
)

data class AuthorWithBooks(
    val author: AuthorData,
    val books: List<AozoraBookCard>,
)
