/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

data class CachedBookModel(
    val id: String,
    val groupId: String,
    val title: String,
    val titleKana: String,
    val authorName: String?,
    val zipUrl: String?,
    val htmlUrl: String?,
)

data class BookWithProgress(
    val book: CachedBookModel,
    val progress: ReadProgress,
    val isUserMarkCompleted: Boolean,
)
