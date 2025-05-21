/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator

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
        rememberCircuitNavigator(backStack, onRootPop = {}, enableBackHandler = true)

    val current = backStack.topRecord?.screen
    val isRoot = current == LibraryNestedScreen
    val currentNavigation =
        when (current) {
            LibraryNestedScreen -> NavigationItem.LIBRARY
            SearchNestedScreen -> NavigationItem.SEARCH
            else -> NavigationItem.LIBRARY
        }
    Scaffold(
        modifier = modifier,
        topBar = {
            AozoraAppBar(
                currentNavigation,
                onClickMore = {
                    onEvent.invoke(HomeUiEvent.OnClickMore)
                },
            )
        },
        bottomBar = {
            AozoraNavigationBar(
                selectedItem = currentNavigation,
                onSelectItem = {
                    if (it == currentNavigation) return@AozoraNavigationBar

                    if (isRoot) {
                        navigator.goTo(it.toScreen())
                    } else {
                        navigator.pop()
                    }
                },
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AozoraAppBar(
    currentNavigation: NavigationItem,
    modifier: Modifier = Modifier,
    onClickMore: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            if (currentNavigation == NavigationItem.LIBRARY) {
                Text("青空読書")
            } else {
                Text(currentNavigation.label)
            }
        },
        actions = {
            if (currentNavigation == NavigationItem.LIBRARY) {
                IconButton(
                    modifier = Modifier.padding(end = 12.dp),
                    onClick = onClickMore,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null,
                    )
                }
            }
        },
    )
}

@Composable
fun AozoraNavigationBar(
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
                selected = isSelected,
                onClick = {
                    onSelectItem(item)
                },
            )
        }
    }
}

enum class NavigationItem {
    LIBRARY,
    SEARCH,
}

fun NavigationItem.toScreen() =
    when (this) {
        NavigationItem.LIBRARY -> LibraryNestedScreen
        NavigationItem.SEARCH -> SearchNestedScreen
    }

val NavigationItem.icon
    get() =
        when (this) {
            NavigationItem.LIBRARY -> Icons.Outlined.Book
            NavigationItem.SEARCH -> Icons.Outlined.Search
        }

val NavigationItem.selectedIcon
    get() =
        when (this) {
            NavigationItem.LIBRARY -> Icons.Filled.Book
            NavigationItem.SEARCH -> Icons.Filled.Search
        }

val NavigationItem.label
    get() =
        when (this) {
            NavigationItem.LIBRARY -> "Library"
            NavigationItem.SEARCH -> "検索"
        }
