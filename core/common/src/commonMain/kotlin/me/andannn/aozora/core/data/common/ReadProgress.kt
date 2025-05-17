/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

sealed interface ReadProgress {
    /**
     * Open book first time and not yet start reading.
     */
    data object None : ReadProgress

    /**
     * Reading book main content.
     *
     * @param blockIndex current block index.
     * @param totalBlockCount total block count.
     */
    data class Reading(
        val blockIndex: Int,
        val totalBlockCount: Int? = null,
    ) : ReadProgress {
        val progressFactor: Float?
            get() =
                totalBlockCount?.let {
                    blockIndex.toFloat() / it.toFloat()
                }
    }

    /**
     * Read the last page of book
     */
    data object Done : ReadProgress
}
