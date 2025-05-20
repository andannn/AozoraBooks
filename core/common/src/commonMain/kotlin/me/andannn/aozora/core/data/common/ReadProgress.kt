/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

/**
 * The book progress which is not read.
 * This number is also present progress of cover page.
 */
const val READ_PROGRESS_NONE = -2

/**
 * The book progress which is completed.
 * This number is also present progress of bibliographical page.
 */
const val READ_PROGRESS_DONE = -1

sealed interface ReadProgress {
    val progressFactor: Float?

    /**
     * Open book first time and not yet start reading.
     */
    data object None : ReadProgress {
        override val progressFactor: Float = 0f
    }

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
        override val progressFactor: Float?
            get() =
                totalBlockCount?.let {
                    blockIndex.toFloat() / it.toFloat()
                }
    }

    /**
     * Read the last page of book
     */
    data object Done : ReadProgress {
        override val progressFactor: Float = 1f
    }
}
