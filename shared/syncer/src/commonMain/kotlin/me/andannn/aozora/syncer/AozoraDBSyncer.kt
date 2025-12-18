/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer

sealed interface SyncResult {
    data object Retry : SyncResult

    data object Success : SyncResult

    data class Fail(
        val e: Exception,
    ) : SyncResult
}

interface AozoraDBSyncer {
    suspend fun sync(force: Boolean = false): SyncResult
}
