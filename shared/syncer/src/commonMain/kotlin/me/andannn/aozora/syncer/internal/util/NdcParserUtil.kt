/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal.util

import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookIdWithBookCategory
import me.andannn.aozora.core.domain.model.asNDCClassification

internal fun BookEntity.parseAsBookIdWithBookCategory(): List<BookIdWithBookCategory> {
    val parsed = categoryNo?.asNDCClassification() ?: emptyList()
    return parsed.map {
        BookIdWithBookCategory(
            bookId = bookId,
            authorId = authorId,
            ndcMainClassNum = it.mainClassNum,
            ndcDivisionNum = it.divisionNum ?: error("NDC division number is required"),
            ndcSectionNum = it.sectionNum ?: error("NDC division number is required"),
        )
    }
}
