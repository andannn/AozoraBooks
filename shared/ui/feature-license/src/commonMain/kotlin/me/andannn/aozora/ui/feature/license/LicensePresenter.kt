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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.LibraryInfo
import me.andannn.aozora.ui.common.Navigator
import me.andannn.aozora.ui.common.RetainedPresenter
import me.andannn.aozora.ui.common.RootNavigator
import me.andannn.aozora.ui.common.retainPresenter
import me.andannn.aozora.ui.common.util.getLicenseInfo

@Stable
data class LicenseState(
    val licenseList: ImmutableList<LibraryInfo>,
    val evenSink: (LicenseUiEvent) -> Unit = {},
)

sealed interface LicenseUiEvent {
    data class OnClickLicense(
        val library: LibraryInfo,
    ) : LicenseUiEvent

    data object OnBack : LicenseUiEvent
}

@Composable
internal fun retainLicensePresenter(
    navigator: Navigator = RootNavigator.current,
    uriHandler: UriHandler = LocalUriHandler.current,
) = retainPresenter(
    navigator,
    uriHandler,
) {
    LicensePresenter(
        navigator = navigator,
        uriHandler = uriHandler,
    )
}

private class LicensePresenter(
    private val navigator: Navigator,
    private val uriHandler: UriHandler,
) : RetainedPresenter<LicenseState>() {
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
