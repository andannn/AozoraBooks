/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.popUntil
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.common.screens.AuthorPagesScreen
import me.andannn.aozora.ui.feature.common.screens.IndexPageScreen
import me.andannn.aozora.ui.feature.common.screens.LibraryNestedScreen
import me.andannn.aozora.ui.feature.common.screens.SearchInputScreen
import me.andannn.aozora.ui.feature.common.screens.SearchNestedScreen
import me.andannn.aozora.ui.feature.common.screens.SearchResultScreen
import kotlin.reflect.KClass

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
        nestedNavigationMap.entries
            .first {
                it.value.any { screen ->
                    screen.screenClass == current::class
                }
            }.key

    val showNavigationBar by rememberUpdatedState(
        current !is SearchInputScreen,
    )
    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedContent(
                showNavigationBar,
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
                            val nestRoot = nestedNavigationMap[it]!!.first { it.isRoot }
                            val alreadyInStack =
                                backStack.iterator().asSequence().firstOrNull { record ->
                                    record.screen::class == nestRoot.screenClass
                                } != null
                            if (alreadyInStack) {
                                navigator.popUntil { screen ->
                                    screen::class == nestRoot.screenClass
                                }
                            } else {
                                navigator.goTo(classToScreenObj(nestRoot.screenClass))
                            }
                        },
                    )
                }
            }
        },
    ) {
        CompositionLocalProvider(
            LocalNavigator provides navigator,
        ) {
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
            )
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

private data class ScreenWithRole(
    val screenClass: KClass<*>,
    val isRoot: Boolean,
)

private val nestedNavigationMap =
    mapOf(
        NavigationItem.LIBRARY to
            listOf(
                ScreenWithRole(LibraryNestedScreen::class, isRoot = true),
            ),
        NavigationItem.SEARCH to
            listOf(
                ScreenWithRole(SearchNestedScreen::class, isRoot = true),
                ScreenWithRole(SearchInputScreen::class, isRoot = false),
                ScreenWithRole(SearchResultScreen::class, isRoot = false),
                ScreenWithRole(SearchResultScreen::class, isRoot = false),
                ScreenWithRole(IndexPageScreen::class, isRoot = false),
                ScreenWithRole(AuthorPagesScreen::class, isRoot = false),
            ),
    )

private fun classToScreenObj(screenClass: KClass<*>) =
    when (screenClass) {
        LibraryNestedScreen::class -> LibraryNestedScreen
        SearchNestedScreen::class -> SearchNestedScreen
        else -> error("Unknown screen class: $screenClass")
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
