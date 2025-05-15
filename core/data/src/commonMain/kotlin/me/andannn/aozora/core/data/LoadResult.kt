/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data

sealed interface LoadResult<T> {
    data class Success<T>(
        val data: T,
    ) : LoadResult<T>

    data class Error<T>(
        val throwable: Throwable,
    ) : LoadResult<T>
}
