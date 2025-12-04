/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.LocalPolice
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.andannn.platform.Platform

@Composable
fun About(
    presenter: AboutPresenter = retainAboutPresenter(),
    modifier: Modifier = Modifier,
) {
    About(
        state = presenter.present(),
        modifier = modifier,
    )
}

@Composable
fun About(
    state: AboutState,
    modifier: Modifier = Modifier,
) {
    AboutContent(
        appVersion = state.appVersion,
        modifier = modifier,
        onEvent = state.evenSink,
        platform = state.platform,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutContent(
    appVersion: String,
    platform: Platform,
    isAndroid: Boolean = true,
    modifier: Modifier = Modifier,
    onEvent: (AboutUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("アプリについて")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent.invoke(AboutUiEvent.OnBack)
                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                SettingItem(
                    imageVector = Icons.Outlined.LocalPolice,
                    title = "利用規約＆プライバシーポリシー",
                    onClick = {
                        onEvent.invoke(AboutUiEvent.OnClickPrivacy)
                    },
                )

                SettingItem(
                    imageVector = Icons.Outlined.LocalPolice,
                    title = "ライセンス",
                    onClick = {
                        onEvent.invoke(AboutUiEvent.OnClickLicense)
                    },
                )

                SettingItem(
                    title = "${if (isAndroid) platform.getLabel() else "iOS"}版 青空読書",
                    subTitle = appVersion,
                    imageVector = Icons.Outlined.Apps,
                    onClick = null,
                )
            }
        }
    }
}

private fun Platform.getLabel(): String =
    when (this) {
        Platform.ANDROID -> "Android"
        Platform.IOS -> "iOS"
    }

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    title: String,
    subTitle: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Container(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            verticalAlignment = CenterVertically,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                if (subTitle != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(subTitle, style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}

@Composable
private fun Container(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (onClick != null) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
        ) {
            content()
        }
    } else {
        Surface(
            modifier = modifier.fillMaxWidth(),
        ) {
            content()
        }
    }
}
