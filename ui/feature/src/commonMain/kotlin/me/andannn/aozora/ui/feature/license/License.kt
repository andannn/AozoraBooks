/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.license

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import me.andannn.aozora.core.data.common.LibraryInfo

@Composable
fun License(
    state: LicenseState,
    modifier: Modifier = Modifier,
) {
    LicenseContent(
        modifier = modifier,
        licenseList = state.licenseList,
        onEvent = state.evenSink,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicenseContent(
    modifier: Modifier = Modifier,
    licenseList: ImmutableList<LibraryInfo>,
    onEvent: (LicenseUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("ライセンス")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent.invoke(LicenseUiEvent.OnBack)
                        },
                    ) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
                    }
                },
            )
        },
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(
                items = licenseList,
                key = { it.artifactId },
            ) { library ->
                LibraryItem(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                    info = library,
                    onClick = {
                        onEvent(LicenseUiEvent.OnClickLicense(library))
                    },
                )
            }
        }
    }
}

@Composable
private fun LibraryItem(
    modifier: Modifier = Modifier,
    info: LibraryInfo,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Column {
            Text("${info.artifactId} ${info.version}", style = MaterialTheme.typography.headlineSmall)
            info.spdxLicenses.forEach { spdx ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(spdx.name, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
