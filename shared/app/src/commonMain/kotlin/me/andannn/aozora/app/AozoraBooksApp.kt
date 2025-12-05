/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import io.github.aakira.napier.Napier
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.syncer.AozoraDBSyncer
import me.andannn.aozora.ui.common.HomeScreen
import me.andannn.aozora.ui.common.NavigatorImpl
import me.andannn.aozora.ui.common.RootNavigator
import me.andannn.aozora.ui.common.Screen
import me.andannn.aozora.ui.common.buildSavedStateConfiguration
import me.andannn.aozora.ui.common.dialog.ActionDialog
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.rememberPopupControllerNavEntryDecorator
import me.andannn.aozora.ui.common.rememberRetainedValueStoreNavEntryDecorator
import me.andannn.aozora.ui.common.theme.AozoraTheme
import me.andannn.platform.PlatformAnalytics
import org.koin.mp.KoinPlatform.getKoin

private const val TAG = "AozoraBooksApp"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AozoraBooksApp() {
    val backStack =
        rememberNavBackStack(
            buildSavedStateConfiguration(),
            HomeScreen,
        )
    val navigator = retain { NavigatorImpl() }

    DisposableEffect(backStack) {
        navigator.backStack = backStack

        onDispose {
            navigator.backStack = null
        }
    }

    // Log screen transition event.
    val currentScreen = backStack.lastOrNull()
    LaunchedEffect(currentScreen) {
        if (currentScreen != null && currentScreen is Screen) {
            Napier.d(tag = TAG) { "log screen Event $currentScreen" }
            // Log screen transition.
            getKoin()
                .get<PlatformAnalytics>()
                .logScreenEvent(currentScreen)
        }
    }
    AozoraTheme(
        dynamicColor = false,
    ) {
        CompositionLocalProvider(
            RootNavigator provides navigator,
        ) {
            NavDisplay(
                modifier = Modifier,
                backStack = backStack,
                sceneStrategy = DialogSceneStrategy<NavKey>() then SinglePaneSceneStrategy(),
                entryDecorators =
                    listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberRetainedValueStoreNavEntryDecorator(),
                        rememberPopupControllerNavEntryDecorator(),
                    ),
                entryProvider = aosoraScreenEntryProvider(),
            )
        }
    }

    LaunchedEffect(Unit) {
        forceSyncAozoraDbIfNeeded()
    }
}

private fun PlatformAnalytics.logScreenEvent(screen: Screen) {
    logEvent(
        event = "ScreenTransition",
        params =
            mapOf(
                "screen" to screen.toString(),
            ),
    )
}

private suspend fun forceSyncAozoraDbIfNeeded() {
    val ndcTableMigrated = getKoin().get<UserDataRepository>().isNdcTableMigrated()

    if (!ndcTableMigrated) {
        Napier.d(tag = TAG) { "forceSyncAozora start." }
        val syncer = getKoin().get<AozoraDBSyncer>()
        syncer.sync(force = true)
    }
}
