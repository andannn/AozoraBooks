/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal

import aosora.core.syncer.generated.resources.Res
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.first
import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import me.andannn.aozora.core.database.dao.BookLibraryDao
import me.andannn.aozora.core.database.entity.AuthorEntity
import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.database.entity.BookIdWithBookCategory
import me.andannn.aozora.core.datastore.UserSettingPreferences
import me.andannn.aozora.syncer.AozoraDBSyncer
import me.andannn.aozora.syncer.SyncResult
import me.andannn.aozora.syncer.internal.util.CSV_ZIP_URL
import me.andannn.aozora.syncer.internal.util.SyncEvent
import me.andannn.aozora.syncer.internal.util.getCsvZipLastModifiedTime
import me.andannn.aozora.syncer.internal.util.logSyncEvent
import me.andannn.aozora.syncer.internal.util.parseAsBookIdWithBookCategory
import me.andannn.aozora.syncer.internal.util.parseAsBookModel
import me.andannn.aozora.syncer.internal.util.toEntity
import me.andannn.core.util.downloadTo
import me.andannn.core.util.unzipTo
import me.andannn.core.util.writeToPath
import me.andannn.platform.PlatformAnalytics
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.mp.KoinPlatform.getKoin

private const val BUNDLED_CSV_FILE_LAST_MODIFIED_TIME = "Sat, 31 May 2025 18:03:00 GMT"

private const val TAG = "AozoraDBSyncer"

internal class AozoraDBSyncerImpl(
    private val dao: BookLibraryDao,
    private val userSettingPreferences: UserSettingPreferences,
    private val analytics: PlatformAnalytics,
) : AozoraDBSyncer {
    override suspend fun sync(force: Boolean): SyncResult {
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

                analytics.logSyncEvent(SyncEvent.SuccessBundle)
                return SyncResult.Retry
            }

            val serverLastModifiedTime = getCsvZipLastModifiedTime()
            if (force || lastSuccessfulSyncTime != serverLastModifiedTime) {
                Napier.d(tag = TAG) { "Sync with server data E" }
                syncWithAozoraServerData()
                saveLastSuccessfulSyncTime(serverLastModifiedTime)
                analytics.logSyncEvent(SyncEvent.SuccessServer)
                Napier.d(tag = TAG) { "Sync with server data X" }
            } else {
                Napier.d(tag = TAG) { "sync time is same as server time. No need to sync." }
                analytics.logSyncEvent(SyncEvent.Skip)
                return SyncResult.Success
            }

            Napier.d(tag = TAG) { "sync end." }
            return SyncResult.Success
        } catch (e: Exception) {
            Napier.e(tag = TAG) { "sync error. $e" }
            analytics.logSyncEvent(SyncEvent.Fail)
            return SyncResult.Fail(e)
        }
    }

    private suspend fun syncWithAppBundledData() {
        // save app bundled csv to local.
        val csvZipPath = Path(getCachedCsvPath(), CSV_TEMP_ZIP_FILE_NAME)
        Buffer().apply { write(bundledCsvZip()) }.writeToPath(csvZipPath)

        unzipCsvAndInsertToDB()
    }

    private suspend fun syncWithAozoraServerData() {
        val csvZipPath = Path(getCachedCsvPath(), CSV_TEMP_ZIP_FILE_NAME)
        getKoin().get<HttpClient>().downloadTo(CSV_ZIP_URL, csvZipPath)

        unzipCsvAndInsertToDB()
    }

    private suspend fun unzipCsvAndInsertToDB() {
        val csvDictionary = getCachedCsvPath()
        val zipPath = Path(csvDictionary, CSV_TEMP_ZIP_FILE_NAME)
        zipPath.unzipTo(csvDictionary)
        val csvFile = SystemFileSystem.list(csvDictionary).firstOrNull { it.name.endsWith(".csv") }
        if (csvFile == null) {
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
        val insertedAuthorIds = mutableSetOf<String>()

        this.chunked(2000).forEach { bookList ->
            val authorListToUpsert = mutableListOf<AuthorEntity>()
            val bookIdWithBookCategoryToUpsert = mutableListOf<BookIdWithBookCategory>()
            for (book in bookList) {
                val authorId = book.authorId
                if (insertedAuthorIds.add(authorId)) {
                    val author = book.toAuthorEntity()
                    authorListToUpsert.add(author)
                }

                bookIdWithBookCategoryToUpsert.addAll(
                    book.parseAsBookIdWithBookCategory(),
                )
            }

            dao.upsertBookAndAuthorList(
                bookList,
                authorListToUpsert,
                bookIdWithBookCategoryToUpsert,
            )
        }
    }

    private suspend fun saveLastSuccessfulSyncTime(time: String) {
        userSettingPreferences.setLastSuccessfulSyncTime(time)
    }
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun bundledCsvZip(): ByteArray = Res.readBytes("files/csv/list_person_all_extended_utf8.zip")

private const val CSV_TEMP_ZIP_FILE_NAME = "temp.zip"

internal expect fun getCachedCsvPath(): Path

private fun BookEntity.toAuthorEntity() =
    AuthorEntity(
        authorId = authorId,
        lastName = authorLastName,
        firstName = authorFirstName,
        lastNameKana = authorLastNameKana,
        firstNameKana = authorFirstNameKana,
        lastNameSortKana = authorLastNameSortKana,
        firstNameSortKana = authorFirstNameSortKana,
        lastNameRomaji = authorLastNameRomaji,
        firstNameRomaji = authorFirstNameRomaji,
        birth = authorBirth,
        death = authorDeath,
        copyrightFlag = authorCopyrightFlag,
    )
