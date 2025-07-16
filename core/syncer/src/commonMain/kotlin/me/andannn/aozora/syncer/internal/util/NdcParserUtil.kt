/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal.util

import io.github.aakira.napier.Napier
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookIdWithBookCategory
import me.andannn.aozora.core.domain.model.NDCClassification

internal fun BookEntity.parseAsBookIdWithBookCategory(): List<BookIdWithBookCategory> {
    val parsed = categoryNo?.asNDCClassification() ?: emptyList()
    return parsed.map {
        BookIdWithBookCategory(
            bookId = bookId,
            ndcMainClassNum = it.mainClassNum,
            ndcDivisionNum = it.divisionNum ?: error("NDC division number is required"),
            ndcSectionNum = it.sectionNum ?: error("NDC division number is required"),
        )
    }
}

/**
 * Parses the NDC classification from a string.
 *
 * Patterns:
 * - "NDC 931" -> [NDCClassification("931")]
 * - "NDC 931 123" -> [NDCClassification("931"), NDCClassification("123")]
 */
internal fun String.asNDCClassification(): List<NDCClassification> =
    split(" ")
        .let { splitList ->
            if (splitList.size >= 2) {
                splitList
                    .subList(1, splitList.size)
                    .filter { it.isNotBlank() && it.isNotEmpty() }
                    .map {
                        NDCClassification(it.parseDigits())
                    }
            } else {
                Napier.e { "Invalid NDC classification format: $this" }
                emptyList()
            }
        }

private fun String.parseDigits() =
    this.filter {
        it.isDigit()
    }
