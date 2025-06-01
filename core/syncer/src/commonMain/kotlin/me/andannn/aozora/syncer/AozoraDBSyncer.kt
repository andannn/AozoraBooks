package me.andannn.aozora.syncer

sealed interface SyncResult {
    data object Retry : SyncResult

    data object Success : SyncResult

    data class Fail(val e: Exception) : SyncResult
}

interface AozoraDBSyncer {
    suspend fun sync(): SyncResult
}
