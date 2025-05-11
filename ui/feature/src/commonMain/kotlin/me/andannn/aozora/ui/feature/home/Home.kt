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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.andannn.aozora.ui.feature.home.library.Library
import me.andannn.aozora.ui.feature.home.library.rememberLibraryPresenter
import me.andannn.aozora.ui.feature.home.search.Search
import me.andannn.aozora.ui.feature.home.search.rememberSearchPresenter

@Composable
fun Home(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    HomeContent(
        modifier = modifier,
        currentNavigation = state.currentNavigation,
        onEvent = state.evenSink,
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    currentNavigation: NavigationItem,
    onEvent: (HomeUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AozoraAppBar(currentNavigation)
        },
        bottomBar = {
            AozoraNavigationBar(
                selectedItem = currentNavigation,
                onSelectItem = {
                    onEvent.invoke(HomeUiEvent.OnNavigationItemClick(it))
                },
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (currentNavigation) {
                NavigationItem.LIBRARY -> {
                    Library(
                        rememberLibraryPresenter().present(),
                    )
                }

                NavigationItem.SEARCH -> {
                    Search(
                        state = rememberSearchPresenter().present(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AozoraAppBar(
    currentNavigation: NavigationItem,
    modifier: Modifier = Modifier,
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
