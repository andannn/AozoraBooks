package me.andannn.aozora.syncer

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.github.aakira.napier.Napier
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

object SyncWorkHelper {
    private const val PERIODIC_SYNC_WORK_NAME = "periodic_sync_work_name"
    private const val ONE_TIME_SYNC_WORK_NAME = "one_time_sync_work_name"

    fun registerPeriodicSyncWork(context: Context) {
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(48, TimeUnit.HOURS)
                .build()

        val workManager = WorkManager.getInstance(context = context)
        workManager.enqueueUniquePeriodicWork(
            PERIODIC_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest,
        )
    }

    fun doOneTimeSyncWork(context: Context) {
        val oneTimeWorkRequest =
            OneTimeWorkRequestBuilder<SyncWorker>()
                .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            ONE_TIME_SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            oneTimeWorkRequest,
        )
    }
}

private const val TAG = "SyncWorker"

internal class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params),
    KoinComponent {
    private val syncer: AozoraDBSyncer by inject()

    override suspend fun doWork(): Result {
        Napier.d(tag = TAG) { "worker start" }
        val result = syncer.sync()
        return result.toWorkerResult().also {
            Napier.d(tag = TAG) { "worker end. $result" }
        }
    }
}

private fun SyncResult.toWorkerResult(): ListenableWorker.Result =
    when (this) {
        is SyncResult.Fail -> ListenableWorker.Result.success()
        SyncResult.Retry -> ListenableWorker.Result.retry()
        SyncResult.Success -> ListenableWorker.Result.failure()
    }
