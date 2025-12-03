/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.embedded

import androidx.room.Embedded
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookProgressEntity

data class BookEntityWithProgress(
    @Embedded val book: BookEntity,
    @Embedded val progress: BookProgressEntity?,
)
