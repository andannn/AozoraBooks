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
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.andannn.RetainedModel
import io.github.andannn.retainRetainedModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NDCType
import me.andannn.aozora.core.domain.model.NdcDataWithBookCount
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.common.screens.BookCardScreen
import me.andannn.aozora.ui.feature.common.screens.NdcContentScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun retainNdcContentPresenter(
    ndcString: String,
    navigator: Navigator = LocalNavigator.current,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
) = retainRetainedModel(
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

class NdcContentPresenter(
    private val ndcClassification: NDCClassification,
    private val navigator: Navigator,
    private val aozoraContentsRepository: AozoraContentsRepository,
) : RetainedModel(),
    Presenter<NdcContentState> {
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
            val pagingDataFlow =
                rememberRetained {
                    aozoraContentsRepository
                        .getBookEntitiesOfNdcClassificationFlow(
                            ndcClassification,
                        ).cachedIn(retainedScope)
                }
            val pagingData = pagingDataFlow.collectAsLazyPagingItems()
            return NdcContentState.NdcDetails(
                title = title,
                pagingData = pagingData,
                evenSink = eventSink,
            )
        } else {
            val children =
                produceRetainedState(emptyList()) {
                    value =
                        aozoraContentsRepository
                            .getChildrenOfNDC(ndcClassification)
                            .filter { it.bookCount != 0 }
                            .sortedBy { it.ndcData.ndcClassification.value }
                }
            return NdcContentState.NdcChildren(
                title = title,
                children = children.value,
                evenSink = eventSink,
            )
        }
    }
}

@Stable
sealed class NdcContentState(
    open val title: String,
    open val evenSink: (NdcContentUiEvent) -> Unit,
) : CircuitUiState {
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
