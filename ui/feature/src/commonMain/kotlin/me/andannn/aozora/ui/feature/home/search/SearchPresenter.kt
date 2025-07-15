/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NdcData
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.common.navigator.RootNavigator
import me.andannn.aozora.ui.feature.common.screens.AuthorPagesScreen
import me.andannn.aozora.ui.feature.common.screens.IndexPageScreen
import me.andannn.aozora.ui.feature.common.screens.NdcContentScreen
import me.andannn.aozora.ui.feature.common.screens.SearchInputResult
import me.andannn.aozora.ui.feature.common.screens.SearchInputScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun rememberSearchPresenter(
    rootNavigator: Navigator = RootNavigator.current,
    localNavigator: Navigator = LocalNavigator.current,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
): SearchPresenter =
    remember(
        rootNavigator,
        localNavigator,
        aozoraContentsRepository,
    ) {
        SearchPresenter(
            rootNavigator = rootNavigator,
            localNavigator = localNavigator,
            aozoraContentsRepository = aozoraContentsRepository,
        )
    }

private const val TAG = "SearchPresenter"

class SearchPresenter(
    private val rootNavigator: Navigator,
    private val localNavigator: Navigator,
    private val aozoraContentsRepository: AozoraContentsRepository,
) : Presenter<SearchState> {
    @Composable
    override fun present(): SearchState {
        val searchInputNavigator =
            rememberAnsweringNavigator<SearchInputResult>(localNavigator) { result ->
                Napier.d(tag = TAG) { "navigator result: ${result.inputText}" }
                // Navigate to search result Page.
            }

        val ndcRootCategoryList =
            produceRetainedState(persistentListOf()) {
                val ndcClassificationList =
                    List(10) { index ->
                        NDCClassification(index.toString())
                    }

                value =
                    ndcClassificationList
                        .mapNotNull {
                            aozoraContentsRepository.getNDCDetails(it)
                        }.toImmutableList()
            }

        return SearchState(
            ndcRootCategoryList = ndcRootCategoryList.value,
        ) { event ->
            when (event) {
                is SearchUiEvent.OnClickKanaItem -> {
                    localNavigator.goTo(IndexPageScreen(kana = event.kana.kanaLabel))
                }

                is SearchUiEvent.OnClickKanaLineItem -> {
                    localNavigator.goTo(AuthorPagesScreen(code = event.lineItem.code))
                }

                SearchUiEvent.OnSearchBarClick -> {
                    searchInputNavigator.goTo(SearchInputScreen(initialParam = null))
                }

                is SearchUiEvent.OnClickNdcItem -> {
                    localNavigator.goTo(NdcContentScreen(event.ndcData.ndcClassification.value))
                }
            }
        }
    }
}

@Stable
data class SearchState(
    val ndcRootCategoryList: ImmutableList<NdcData>,
    val evenSink: (SearchUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface SearchUiEvent {
    data class OnClickKanaItem(
        val kana: KanaItem,
    ) : SearchUiEvent

    data class OnClickKanaLineItem(
        val lineItem: KanaLineItem,
    ) : SearchUiEvent

    data class OnClickNdcItem(
        val ndcData: NdcData,
    ) : SearchUiEvent

    data object OnSearchBarClick : SearchUiEvent
}
