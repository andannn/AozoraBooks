package me.andannn.aosora.core.pagesource

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.collections.plus

private const val TAG = "LazyBookPageSource"

/**
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class LazyBookPageSource<T>(
    scope: CoroutineScope,
    private val settledPageFlow: Flow<T?>
) : BookPageSource<T>, CoroutineScope by scope {
    private val pageListFlow = MutableStateFlow<List<T>>(emptyList())

    override val pagerSnapShotFlow = MutableSharedFlow<PagerSnapShot<T>>()

    private val beforePageCountFlow =
        combine(settledPageFlow, pageListFlow) { settledPage, pageList ->
            pageList.indexOf(settledPage)
        }

    private val afterPageCountFlow =
        combine(settledPageFlow, pageListFlow) { settledPage, pageList ->
            val currentIndexOfSettledPage = pageList.indexOf(settledPage)
            if (currentIndexOfSettledPage == -1) {
                // current page is unsettled.
                -1
            } else {
                (pageList.count() - 1 - currentIndexOfSettledPage).coerceAtLeast(0)
            }
        }

    private var notifyTask: DelayCancelNotifyTask? = null
    private var version: Int = 0

    init {
        launch {
            generatePageFlowAfter()
                .flowOn(Dispatchers.IO)
                .chunked(OneTimeLoadSize) // emit every 5 pages.
                .collect {
                    addToAfter(it)
                    yield()
                    Napier.d(tag = TAG) { "in after await E." }
                    afterPageCountFlow.awaitUntilLessThan(OneTimeLoadSize)
                    Napier.d(tag = TAG) { "in after await X." }
                }
            Napier.d(tag = TAG) { "all page after is loaded." }
        }

        launch {
            // wait for settled page
            awaitSettledPage()

            generatePageFlowBefore()
                .flowOn(Dispatchers.IO)
                .chunked(OneTimeLoadSize) // emit every 5 pages.
                .collect {
                    addToBefore(it)
                    yield()
                    Napier.d(tag = TAG) { "in before await E." }
                    beforePageCountFlow.awaitUntilLessThan(OneTimeLoadSize)
                    Napier.d(tag = TAG) { "in before await X." }
                }
            Napier.d(tag = TAG) { "all page before is loaded." }
        }
    }

    abstract fun generatePageFlowBefore(): Flow<T>

    abstract fun generatePageFlowAfter(): Flow<T>

    private suspend fun awaitSettledPage() = settledPageFlow.first { it != null }

    private suspend fun Flow<Int>.awaitUntilLessThan(limit: Int) = this.first { count ->
        count != -1 && count < limit
    }

    private fun addToBefore(page: List<T>) {
        Napier.d(tag = TAG) { "add new page before: ${page.reversed().map { it.hashCode() }}" }
        pageListFlow.update {
            page.reversed() + it
        }
        notifySnapshotPagerChanged(addBefore = true)
    }

    private fun addToAfter(page: List<T>) {
        Napier.d(tag = TAG) { "add new page after: ${page.map { it.hashCode() }}" }
        pageListFlow.update {
            it + page
        }
        notifySnapshotPagerChanged(addBefore = false)
    }

    private fun notifySnapshotPagerChanged(addBefore: Boolean) {
        val currentTask = notifyTask ?: DelayCancelNotifyTask(this).also { notifyTask = it }

        currentTask.notifyDelayed(
            addBefore = addBefore,
            onNotify = { needChangeInitialIndex ->
                Napier.d(tag = TAG) { "notify pager changed. needChangeInitialIndex $needChangeInitialIndex" }
                val current = pageListFlow.value.indexOf(settledPageFlow.firstOrNull())
                val index = if (current == -1) null else current
                pagerSnapShotFlow.emit(
                    PagerSnapShot(
                        pageList = pageListFlow.value,
                        initialIndex = index,
                        snapshotVersion = version++
                    )
                )

                notifyTask = null
            }
        )
    }

    private class DelayCancelNotifyTask(
        scope: CoroutineScope,
        private val delayMs: Long = 500,
    ) : CoroutineScope by scope {
        private var needResetInitialIndex = false
        private var notifyJob: Job? = null

        /**
         * Delay [delayMs] and call [onNotify].
         * If [notifyDelayed] is called again within [delayMs], the previous job is canceled and a new one is scheduled.
         */
        fun notifyDelayed(
            addBefore: Boolean,
            onNotify: suspend (needResetInitialIndex: Boolean) -> Unit,
        ) {
            if (addBefore) {
                needResetInitialIndex = true
            }

            notifyJob?.cancel()
            notifyJob = launch {
                delay(delayMs)

                onNotify.invoke(needResetInitialIndex)
            }
        }
    }

    companion object {
        private const val OneTimeLoadSize = 5
    }
}
