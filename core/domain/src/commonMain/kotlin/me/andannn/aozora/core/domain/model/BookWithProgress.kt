/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

data class BookWithProgress(
    val book: AozoraBookCard,
    val progress: ReadProgress,
    val isUserMarkCompleted: Boolean,
)
