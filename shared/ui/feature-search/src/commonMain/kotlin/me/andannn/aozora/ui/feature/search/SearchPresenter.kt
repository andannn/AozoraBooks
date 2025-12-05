/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.KanaLineItem
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NdcData
import me.andannn.aozora.core.domain.repository.AozoraContentsRepository
import me.andannn.aozora.ui.common.AuthorPagesScreen
import me.andannn.aozora.ui.common.IndexPageScreen
import me.andannn.aozora.ui.common.LocalNavigator
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.NdcContentScreen
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.SearchInputScreen
import me.andannn.aozora.ui.common.retainPresenter
import org.koin.mp.KoinPlatform.getKoin

@Composable
internal fun retainSearchPresenter(
    localNavigator: Navigator = LocalNavigator.current,
    aozoraContentsRepository: AozoraContentsRepository = getKoin().get(),
) = retainPresenter(
    localNavigator,
    aozoraContentsRepository,
) {
    SearchPresenter(
        localNavigator = localNavigator,
        aozoraContentsRepository = aozoraContentsRepository,
    )
}

private const val TAG = "SearchPresenter"

private class SearchPresenter(
    private val localNavigator: Navigator,
    private val aozoraContentsRepository: AozoraContentsRepository,
) : RetainedPresenter<SearchState>() {
    val ndcRootCategoryListFlow = MutableStateFlow<ImmutableList<NdcData>>(persistentListOf())

    init {
        retainedScope.launch {
            val ndcClassificationList =
                List(10) { index ->
                    NDCClassification(index.toString())
                }

            ndcRootCategoryListFlow.value =
                ndcClassificationList
                    .mapNotNull {
                        aozoraContentsRepository.getNDCDetails(it)
                    }.toImmutableList()
        }
    }

    @Composable
    override fun present(): SearchState {
        val ndcRootCategoryList by ndcRootCategoryListFlow.collectAsStateWithLifecycle()

        return SearchState(
            ndcRootCategoryList = ndcRootCategoryList,
        ) { event ->
            when (event) {
                is SearchUiEvent.OnClickKanaItem -> {
                    localNavigator.goTo(IndexPageScreen(kana = event.kana.kanaLabel))
                }

                is SearchUiEvent.OnClickKanaLineItem -> {
                    localNavigator.goTo(AuthorPagesScreen(code = event.lineItem.code))
                }

                SearchUiEvent.OnSearchBarClick -> {
                    localNavigator.goTo(SearchInputScreen(initialParam = null))
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
)

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
