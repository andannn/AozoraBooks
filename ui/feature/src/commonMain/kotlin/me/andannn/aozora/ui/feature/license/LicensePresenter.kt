/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.license

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.common.LibraryInfo
import me.andannn.aozora.ui.common.navigator.LocalNavigator

@Composable
fun rememberLicensePresenter(
    navigator: Navigator = LocalNavigator.current,
    uriHandler: UriHandler = LocalUriHandler.current,
) = remember(
    navigator,
    uriHandler,
) {
    LicensePresenter(
        navigator = navigator,
        uriHandler = uriHandler,
    )
}

class LicensePresenter(
    private val navigator: Navigator,
    private val uriHandler: UriHandler,
) : Presenter<LicenseState> {
    @Composable
    override fun present(): LicenseState {
        val licenseList =
            produceRetainedState(emptyList<LibraryInfo>()) {
                value = getLicenseInfo()
            }

        return LicenseState(
            licenseList = licenseList.value.toImmutableList(),
        ) { event ->
            when (event) {
                LicenseUiEvent.OnBack -> {
                    navigator.pop()
                }

                is LicenseUiEvent.OnClickLicense -> {
                    uriHandler.openUri(
                        event.library.spdxLicenses
                            .firstOrNull()
                            ?.url ?: return@LicenseState,
                    )
                }
            }
        }
    }
}

@Stable
data class LicenseState(
    val licenseList: ImmutableList<LibraryInfo>,
    val evenSink: (LicenseUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface LicenseUiEvent {
    data class OnClickLicense(
        val library: LibraryInfo,
    ) : LicenseUiEvent

    data object OnBack : LicenseUiEvent
}
