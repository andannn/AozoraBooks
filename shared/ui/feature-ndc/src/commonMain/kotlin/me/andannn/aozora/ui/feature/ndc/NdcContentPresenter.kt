/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.ndc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.andannn.retainRetainedModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NDCType
import me.andannn.aozora.core.domain.model.NdcDataWithBookCount
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.BookCardScreen
import me.andannn.aozora.ui.common.LocalNavigator
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.NdcContentScreen
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.retainPresenter
import org.koin.mp.KoinPlatform.getKoin

@Stable
sealed class NdcContentState(
    open val title: String,
    open val evenSink: (NdcContentUiEvent) -> Unit,
) {
    data class NdcChildren(
        override val title: String,
        val children: List<NdcDataWithBookCount>,
        override val evenSink: (NdcContentUiEvent) -> Unit,
    ) : NdcContentState(title, evenSink)

    data class NdcDetails(
        val pagingData: LazyPagingItems<AozoraBookCard>,
        override val title: String,
        override val evenSink: (NdcContentUiEvent) -> Unit,
    ) : NdcContentState(title, evenSink)
}

sealed interface NdcContentUiEvent {
    object Back : NdcContentUiEvent

    data class OnNdcItemClick(
        val ndcClassification: NDCClassification,
    ) : NdcContentUiEvent

    data class OnBookClick(
        val bookCard: AozoraBookCard,
    ) : NdcContentUiEvent
}

@Composable
internal fun retainNdcContentPresenter(
    ndcString: String,
    navigator: Navigator = LocalNavigator.current,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
) = retainPresenter(
    ndcString,
    navigator,
    aozoraContentsRepository,
) {
    NdcContentPresenter(
        ndcClassification = NDCClassification(ndcString),
        navigator = navigator,
        aozoraContentsRepository = aozoraContentsRepository,
    )
}

private class NdcContentPresenter(
    private val ndcClassification: NDCClassification,
    private val navigator: Navigator,
    private val aozoraContentsRepository: AozoraContentsRepository,
) : RetainedPresenter<NdcContentState>() {
    val titleFlow = MutableStateFlow<String>("")

    init {
        retainedScope.launch {
            titleFlow.value = aozoraContentsRepository.getNDCDetails(ndcClassification)?.label ?: ""
        }
    }

    @Composable
    override fun present(): NdcContentState {
        val title by titleFlow.collectAsStateWithLifecycle()
        val isDetail = ndcClassification.ndcType == NDCType.SECTION

        val eventSink: (NdcContentUiEvent) -> Unit = { event ->
            when (event) {
                is NdcContentUiEvent.Back -> {
                    navigator.pop()
                }

                is NdcContentUiEvent.OnNdcItemClick -> {
                    navigator.goTo(NdcContentScreen(event.ndcClassification.value))
                }

                is NdcContentUiEvent.OnBookClick -> {
                    navigator.goTo(
                        BookCardScreen(
                            bookCardId = event.bookCard.id,
                            groupId = event.bookCard.authorId,
                        ),
                    )
                }
            }
        }

        if (isDetail) {
            val state =
                retainNdcDetailPresenter(
                    ndcClassification = ndcClassification,
                ).present()
            return NdcContentState.NdcDetails(
                title = title,
                pagingData = state.pagingData,
                evenSink = eventSink,
            )
        } else {
            val children =
                retainNdcChildrenPresenter(
                    ndcClassification = ndcClassification,
                    aozoraContentsRepository = aozoraContentsRepository,
                ).present().list
            return NdcContentState.NdcChildren(
                title = title,
                children = children,
                evenSink = eventSink,
            )
        }
    }
}

@Composable
private fun retainNdcDetailPresenter(
    ndcClassification: NDCClassification,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
) = retainRetainedModel(
    ndcClassification,
    aozoraContentsRepository,
) {
    NdcDetailPresenter(
        aozoraContentsRepository = aozoraContentsRepository,
        ndcClassification = ndcClassification,
    )
}

private data class NdcDetails(
    val pagingData: LazyPagingItems<AozoraBookCard>,
)

private class NdcDetailPresenter(
    private val aozoraContentsRepository: AozoraContentsRepository,
    private val ndcClassification: NDCClassification,
) : RetainedPresenter<NdcDetails>() {
    val pagingDataFlow =
        aozoraContentsRepository
            .getBookEntitiesOfNdcClassificationFlow(
                ndcClassification,
            ).cachedIn(retainedScope)

    @Composable
    override fun present(): NdcDetails {
        val pagingData = pagingDataFlow.collectAsLazyPagingItems()
        return NdcDetails(
            pagingData,
        )
    }
}

@Composable
fun retainNdcChildrenPresenter(
    ndcClassification: NDCClassification,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
) = retainPresenter(
    ndcClassification,
    aozoraContentsRepository,
) {
    NdcChildrenPresenter(
        aozoraContentsRepository = aozoraContentsRepository,
        ndcClassification = ndcClassification,
    )
}

data class NdcChildren(
    val list: List<NdcDataWithBookCount>,
)

private class NdcChildrenPresenter(
    val aozoraContentsRepository: AozoraContentsRepository,
    val ndcClassification: NDCClassification,
) : RetainedPresenter<NdcChildren>() {
    val listFlow = MutableStateFlow(emptyList<NdcDataWithBookCount>())

    init {
        retainedScope.launch {
            listFlow.value =
                aozoraContentsRepository
                    .getChildrenOfNDC(ndcClassification)
                    .filter { it.bookCount != 0 }
                    .sortedBy { it.ndcData.ndcClassification.value }
        }
    }

    @Composable
    override fun present(): NdcChildren {
        val list by listFlow.collectAsStateWithLifecycle()
        return NdcChildren(
            list,
        )
    }
}
