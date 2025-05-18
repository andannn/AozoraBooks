/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.andannn.aozora.ui.common.navigator.LocalNavigator
import me.andannn.aozora.ui.feature.screens.LicenseScreen
import me.andannn.platform.appVersion

@Composable
fun rememberAboutPresenter(
    navigator: Navigator = LocalNavigator.current,
    uriHandler: UriHandler = LocalUriHandler.current,
) = remember(
    navigator,
    uriHandler,
) {
    AboutPresenter(
        navigator = navigator,
        uriHandler = uriHandler,
    )
}

class AboutPresenter(
    private val navigator: Navigator,
    private val uriHandler: UriHandler,
) : Presenter<AboutState> {
    @Composable
    override fun present(): AboutState =
        AboutState(
            appVersion = appVersion,
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

@Stable
data class AboutState(
    val appVersion: String,
    val evenSink: (AboutUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface AboutUiEvent {
    data object OnBack : AboutUiEvent

    data object OnClickLicense : AboutUiEvent

    data object OnClickPrivacy : AboutUiEvent
}
