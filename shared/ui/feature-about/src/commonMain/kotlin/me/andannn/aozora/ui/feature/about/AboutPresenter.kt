/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import me.andannn.aozora.ui.common.LicenseScreen
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.RootNavigator
import me.andannn.aozora.ui.common.retainPresenter
import me.andannn.platform.Platform
import me.andannn.platform.appVersion
import me.andannn.platform.platform

@Stable
data class AboutState(
    val appVersion: String,
    val platform: Platform,
    val evenSink: (AboutUiEvent) -> Unit = {},
)

sealed interface AboutUiEvent {
    data object OnBack : AboutUiEvent

    data object OnClickLicense : AboutUiEvent

    data object OnClickPrivacy : AboutUiEvent
}

@Composable
internal fun retainAboutPresenter(
    navigator: Navigator = RootNavigator.current,
    uriHandler: UriHandler = LocalUriHandler.current,
) = retainPresenter(
    navigator,
    uriHandler,
) {
    AboutPresenter(
        navigator = navigator,
        uriHandler = uriHandler,
    )
}

private class AboutPresenter(
    private val navigator: Navigator,
    private val uriHandler: UriHandler,
) : RetainedPresenter<AboutState>() {
    @Composable
    override fun present(): AboutState =
        AboutState(
            appVersion = appVersion,
            platform = platform,
        ) { event ->
            when (event) {
                AboutUiEvent.OnBack -> {
                    navigator.pop()
                }

                AboutUiEvent.OnClickLicense -> {
                    navigator.goTo(LicenseScreen)
                }

                AboutUiEvent.OnClickPrivacy -> {
                    uriHandler.openUri("https://andannn.github.io/ja/aozora-privacy/")
                }
            }
        }
}
