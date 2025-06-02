/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal

import aosora.core.syncer.generated.resources.Res
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import me.andannn.aozora.core.database.dao.SavedBookDao
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.datastore.UserSettingPreferences
import me.andannn.aozora.syncer.AozoraDBSyncer
import me.andannn.aozora.syncer.SyncResult
import me.andannn.aozora.syncer.internal.util.parseAsBookModel
import me.andannn.aozora.syncer.internal.util.toEntity
import me.andannn.core.util.unzipTo
import me.andannn.core.util.writeToPath
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val BUNDLED_CSV_FILE_LAST_MODIFIED_TIME = "Sat, 31 May 2025 18:03:00 GMT"

private const val TAG = "AozoraDBSyncer"

internal class AozoraDBSyncerImpl(
    private val dao: SavedBookDao,
    private val userSettingPreferences: UserSettingPreferences,
) : AozoraDBSyncer {
    override suspend fun sync(): SyncResult {
        try {
            Napier.d(tag = TAG) { "sync started." }
            val lastSuccessfulSyncTime =
                userSettingPreferences.userData.first().lastSuccessfulSyncTime
            if (lastSuccessfulSyncTime == null) {
                Napier.d(tag = TAG) { "Sync with bundled data E" }
                syncWithAppBundledData()
                saveLastSuccessfulSyncTime(BUNDLED_CSV_FILE_LAST_MODIFIED_TIME)
                // Retry to sync with remote data source.
                Napier.d(tag = TAG) { "Sync with bundled data X" }
                return SyncResult.Retry
            }

            Napier.d(tag = TAG) { "sync end." }
            return SyncResult.Success
        } catch (e: Exception) {
            Napier.e(tag = TAG) { "sync error. $e" }
            return SyncResult.Fail(e)
        }
    }

    private suspend fun syncWithAppBundledData() {
        // save app bundled csv to local.
        val csvZipPath = Path(getCachedCsvPath(), CSV_TEMP_ZIP_FILE_NAME)
        Buffer().apply { write(bundledCsvZip()) }.writeToPath(csvZipPath)

        unzipCsvAndInsertToDB()
    }

    private suspend fun unzipCsvAndInsertToDB() {
        val csvDictionary = getCachedCsvPath()
        val zipPath = Path(csvDictionary, CSV_TEMP_ZIP_FILE_NAME)
        zipPath.unzipTo(csvDictionary)
        val csvFile = SystemFileSystem.list(csvDictionary).firstOrNull { it.name.endsWith(".csv") }
        if (csvFile == null) {
            Napier.e(tag = TAG) { "csv file not found" }
            error("csv file not found after unzip")
        }

        SystemFileSystem.source(csvFile).buffered().use { csvSource ->
            csvSource
                .parseAsBookModel()
                .map { it.toEntity() }
                .insertToDB()
        }
        SystemFileSystem.delete(csvFile)
    }

    private suspend fun Sequence<BookEntity>.insertToDB() {
        var insertCount = 0
        this.chunked(2000).forEach { bookList ->
            dao.upsertBookList(bookList)
            insertCount += bookList.size
        }
        Napier.d(tag = TAG) { "insert count: $insertCount" }
    }

    private suspend fun saveLastSuccessfulSyncTime(time: String) {
        userSettingPreferences.setLastSuccessfulSyncTime(time)
    }
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun bundledCsvZip(): ByteArray = Res.readBytes("files/csv/list_person_all_extended_utf8.zip")

private const val CSV_TEMP_ZIP_FILE_NAME = "temp.zip"

internal expect fun getCachedCsvPath(): Path
