/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.popUntil
import com.slack.circuit.runtime.screen.Screen
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.common.screens.LibraryNestedScreen
import me.andannn.aozora.ui.feature.common.screens.SearchInputScreen
import me.andannn.aozora.ui.feature.common.screens.SearchNestedScreen

@Composable
fun Home(
    modifier: Modifier = Modifier,
    presenter: HomePresenter = retainHomePresenter(),
) {
    Home(
        modifier = modifier,
        state = presenter.present(),
    )
}

@Composable
fun Home(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    HomeContent(
        modifier = modifier,
        onEvent = state.evenSink,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val backStack = rememberSaveableBackStack(LibraryNestedScreen)
    val navigator =
        rememberCircuitNavigator(backStack) {
        }
    BackHandler(enabled = backStack.size > 1) {
        navigator.pop()
    }

    val current = backStack.topRecord?.screen ?: LibraryNestedScreen
    val currentNavigation =
        remember(current) {
            val backStackList = backStack.iterator().asSequence().toList()
            val lastBaseRecord =
                backStackList.firstOrNull { record ->
                    record.screen.isNestBase
                }
            lastBaseRecord?.screen?.toNavigationItem()!!
        }

    val showNavigationBar by rememberUpdatedState(
        current !is SearchInputScreen,
    )
    Scaffold(
        modifier = modifier,
    ) {
        Box {
            CompositionLocalProvider(
                LocalNavigator provides navigator,
            ) {
                NavigableCircuitContent(
                    navigator = navigator,
                    backStack = backStack,
                )
            }

            AnimatedContent(
                modifier = Modifier.align(Alignment.BottomStart),
                targetState = showNavigationBar,
                transitionSpec = {
                    if (targetState) {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    } else {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    }
                },
            ) { showNavigationBar ->
                if (showNavigationBar) {
                    AozoraNavigationBar(
                        selectedItem = currentNavigation,
                        onSelectItem = {
                            val nestRootScreen = baseNavigationMap[it]!!
                            val alreadyInStack =
                                backStack.iterator().asSequence().firstOrNull { record ->
                                    record.screen == nestRootScreen
                                } != null
                            if (alreadyInStack) {
                                navigator.popUntil { screen ->
                                    screen == nestRootScreen
                                }
                            } else {
                                navigator.goTo(nestRootScreen)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AozoraNavigationBar(
    modifier: Modifier = Modifier,
    selectedItem: NavigationItem = NavigationItem.LIBRARY,
    onSelectItem: (NavigationItem) -> Unit = {},
) {
    NavigationBar(
        modifier = modifier,
    ) {
        NavigationItem.entries.forEachIndexed { index, item ->
            val isSelected = selectedItem == item
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.icon,
                        contentDescription = null,
                    )
                },
                label = {
                    Text(item.label)
                },
                selected = isSelected,
                onClick = {
                    onSelectItem(item)
                },
            )
        }
    }
}

private enum class NavigationItem {
    LIBRARY,
    SEARCH,
}

private val baseNavigationMap =
    mapOf(
        NavigationItem.LIBRARY to LibraryNestedScreen,
        NavigationItem.SEARCH to SearchNestedScreen,
    )

private val Screen.isNestBase
    get() =
        when (this) {
            is LibraryNestedScreen -> true
            is SearchNestedScreen -> true
            else -> false
        }

private fun Screen.toNavigationItem() =
    when (this) {
        LibraryNestedScreen -> NavigationItem.LIBRARY
        SearchNestedScreen -> NavigationItem.SEARCH
        else -> error("$this is not nest root screen")
    }

private val NavigationItem.icon
    get() =
        when (this) {
            NavigationItem.LIBRARY -> Icons.Outlined.Book
            NavigationItem.SEARCH -> Icons.Outlined.Search
        }

private val NavigationItem.selectedIcon
    get() =
        when (this) {
            NavigationItem.LIBRARY -> Icons.Filled.Book
            NavigationItem.SEARCH -> Icons.Filled.Search
        }

private val NavigationItem.label
    get() =
        when (this) {
            NavigationItem.LIBRARY -> "本棚"
            NavigationItem.SEARCH -> "検索"
        }
