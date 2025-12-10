/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.raw

import kotlinx.coroutines.flow.Flow
import kotlinx.io.files.Path
import me.andannn.aozora.core.pagesource.page.AozoraBlock

/**
 * Book raw source.
 */
internal interface BookRawSource {
    /**
     * Local cache dictionary.
     */
    val cacheDictionary: Path

    /**
     * get raw source of book.
     */
    fun getRawSource(): Flow<AozoraBlock>

    /**
     * get image uri by path.
     */
    suspend fun getImageUriByPath(path: String): Path?

    /**
     * get book meta.
     */
    suspend fun getBookInfo(): BookInfo
}
