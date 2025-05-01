package me.andannn.aozora.ui.common.widgets

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import io.github.aakira.napier.Napier

private const val TAG = "rememberRefreshablePage"

/**
 * Creates a [PagerState] that can be re-initialized when initialPage changed.
 */
@Composable
fun rememberRefreshablePagerState(
    initialPage: Int = 0,
    version: Int?,
    pageCount: () -> Int,
): PagerState =
    rememberSaveable(initialPage, version, saver = DefaultPagerState.Saver) {
        Napier.d(tag = TAG) { "create new pager state: initialPage: $initialPage" }
        DefaultPagerState(
            initialPage,
            pageCount,
        )
    }.apply {
        pageCountState.value = pageCount
    }

private class DefaultPagerState(
    currentPage: Int,
    updatedPageCount: () -> Int,
) : PagerState(currentPage) {
    var pageCountState = mutableStateOf(updatedPageCount)
    override val pageCount: Int get() = pageCountState.value.invoke()

    companion object {
        /**
         * To keep current page and current page offset saved
         */
        val Saver: Saver<DefaultPagerState, *> =
            listSaver(
                save = {
                    listOf(
                        it.currentPage,
                        it.pageCount,
                    )
                },
                restore = {
                    DefaultPagerState(
                        currentPage = it[0] as Int,
                        updatedPageCount = { it[1] as Int },
                    )
                },
            )
    }
}
