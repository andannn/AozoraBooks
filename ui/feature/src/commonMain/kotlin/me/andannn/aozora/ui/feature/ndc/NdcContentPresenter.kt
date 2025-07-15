/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.ndc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NDCType
import me.andannn.aozora.core.domain.model.NdcData
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.common.screens.NdcContentScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberNdcContentPresenter(
    ndcString: String,
    navigator: Navigator = LocalNavigator.current,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
) = remember(
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
) : Presenter<NdcContentState> {
    @Composable
    override fun present(): NdcContentState {
        val title =
            produceRetainedState("") {
                value = aozoraContentsRepository.getNDCDetails(ndcClassification)?.label ?: ""
            }
        val isDetail = ndcClassification.ndcType == NDCType.SECTION

        val eventSink: (NdcContentUiEvent) -> Unit = { event ->
            when (event) {
                is NdcContentUiEvent.Back -> {
                    navigator.pop()
                }

                is NdcContentUiEvent.OnNdcItemClick -> {
                    navigator.goTo(NdcContentScreen(event.ndcClassification.value))
                }
            }
        }

        if (isDetail) {
            return NdcContentState.NdcDetails(
                title = title.value,
                evenSink = eventSink,
            )
        } else {
            val children =
                produceRetainedState(emptyList()) {
                    value =
                        aozoraContentsRepository
                            .getChildrenOfNDC(ndcClassification)
                            .sortedBy { it.ndcClassification.value }
                }
            return NdcContentState.NdcChildren(
                title = title.value,
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
        val children: List<NdcData>,
        override val evenSink: (NdcContentUiEvent) -> Unit,
    ) : NdcContentState(title, evenSink)

    data class NdcDetails(
        override val title: String,
        override val evenSink: (NdcContentUiEvent) -> Unit,
    ) : NdcContentState(title, evenSink)
}

sealed interface NdcContentUiEvent {
    object Back : NdcContentUiEvent

    data class OnNdcItemClick(
        val ndcClassification: NDCClassification,
    ) : NdcContentUiEvent
}
