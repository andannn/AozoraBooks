package me.andannn.aosora.core.pagesource

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

/**
 * Book page source.
 */
interface BookPageSource<T> {
    val pagerSnapShotFlow: Flow<PagerSnapShot<T>>
}

private const val TAG = "BookPageSource"

/**
 */
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
//        settledPageFlow.map {
//        if (it == null) -1 else it
//    }

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

    init {
        launch {
            settledPageFlow.collect {
                Napier.d(tag = TAG) { "settled page is $it" }
            }
        }
        launch {
            pageListFlow.collect {
                Napier.d(tag = TAG) { "pageListFlow is ${it.size}" }
            }
        }
        launch {
            generatePageFlowAfter()
                .flowOn(Dispatchers.IO)
                .collect {
                    addToAfter(it)
                    yield()
                    Napier.d(tag = TAG) { "in after await E." }
                    afterPageCountFlow.awaitUntilLessThan(3)
                    Napier.d(tag = TAG) { "in after await X." }
                }
            Napier.d(tag = TAG) { "all page after is loaded." }
        }

        launch {
            // wait for settled page
            awaitSettledPage()

            generatePageFlowBefore()
                .flowOn(Dispatchers.IO)
                .collect {
                    addToBefore(it)
                    yield()
                    Napier.d(tag = TAG) { "in before await E." }
                    beforePageCountFlow.awaitUntilLessThan(3)
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

    private fun addToBefore(page: T) {
        Napier.d(tag = TAG) { "add new page before: $page" }
        pageListFlow.update {
            listOf(page) + it
        }
        notifySnapshotPagerChanged(addBefore = true)
    }

    private fun addToAfter(page: T) {
        Napier.d(tag = TAG) { "add new page after: $page" }
        pageListFlow.update {
            it + listOf(page)
        }
        notifySnapshotPagerChanged(addBefore = false)
    }

    private fun notifySnapshotPagerChanged(addBefore: Boolean) {
        val currentTask = notifyTask ?: DelayCancelNotifyTask(this).also { notifyTask = it }

        currentTask.notifyDelayed(
            addBefore = addBefore,
            onNotify = { needChangeInitialIndex ->
                Napier.d(tag = TAG) { "notify pager changed. needChangeInitialIndex $needChangeInitialIndex" }
                val index = if (needChangeInitialIndex) {
                    val current = pageListFlow.value.indexOf(settledPageFlow.firstOrNull())
                    if (current == -1) null else current
                } else {
                    null
                }
                pagerSnapShotFlow.emit(
                    PagerSnapShot(
                        pageList = pageListFlow.value,
                        initialIndex = index
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
}

data class PagerSnapShot<T>(
    val initialIndex: Int?,
    val pageList: List<T>,
)

