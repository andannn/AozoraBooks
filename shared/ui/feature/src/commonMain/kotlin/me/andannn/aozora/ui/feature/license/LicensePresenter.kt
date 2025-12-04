/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.license

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import io.github.andannn.RetainedModel
import io.github.andannn.retainRetainedModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.LibraryInfo
import me.andannn.aozora.ui.common.navigator.RootNavigator

@Composable
fun retainLicensePresenter(
    navigator: Navigator = RootNavigator.current,
    uriHandler: UriHandler = LocalUriHandler.current,
) = retainRetainedModel(
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
) : RetainedModel(),
    Presenter<LicenseState> {
    val licenseListFLow = MutableStateFlow(emptyList<LibraryInfo>())

    init {
        retainedScope.launch {
            licenseListFLow.value = getLicenseInfo()
        }
    }

    @Composable
    override fun present(): LicenseState {
        val licenseList = licenseListFLow.collectAsStateWithLifecycle()

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
