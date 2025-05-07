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

@Composable
fun Home(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    HomeContent(
        modifier = modifier,
        state = state,
    )
}

@Composable
fun HomeContent(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AozoraAppBar()
        },
        bottomBar = {
            AozoraNavigationBar(
                selectedItem = state.currentNavigation,
                onSelectItem = {
                    state.evenSink.invoke(HomeUiEvent.OnNavigationItemClick(it))
                },
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (state.currentNavigation) {
                NavigationItem.LIBRARY -> {
                    Library(
                        rememberLibraryPresenter().present(),
                    )
                }

                NavigationItem.SEARCH -> {
                    Search()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AozoraAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text("Aozora Books")
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
                label = { Text(item.label) },
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
            NavigationItem.SEARCH -> "Search"
        }
