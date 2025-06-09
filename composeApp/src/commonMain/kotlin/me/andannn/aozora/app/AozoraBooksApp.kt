/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.animation.AnimatedNavEvent
import com.slack.circuit.foundation.animation.AnimatedNavState
import com.slack.circuit.foundation.animation.AnimatedScreenTransform
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.ExperimentalCircuitApi
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import io.github.aakira.napier.Napier
import me.andannn.aozora.ui.common.dialog.ActionDialog
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController
import me.andannn.aozora.ui.common.navigator.RootNavigator
import me.andannn.aozora.ui.feature.common.screens.HomeScreen
import me.andannn.aozora.ui.feature.common.screens.LibraryNestedScreen
import me.andannn.aozora.ui.feature.common.screens.RoutePresenterFactory
import me.andannn.aozora.ui.feature.common.screens.RouteUiFactory
import me.andannn.aozora.ui.feature.common.screens.SearchNestedScreen
import me.andannn.platform.PlatformAnalytics
import org.koin.mp.KoinPlatform.getKoin

private const val TAG = "AozoraBooksApp"

@Composable
fun AozoraBooksApp(
    modifier: Modifier = Modifier,
    circuit: Circuit = buildCircuitMobile(),
) {
    val backStack = rememberSaveableBackStack(HomeScreen)
    val navigator =
        rememberCircuitNavigator(backStack) {
        }

    // Log screen transition event.
    val currentScreen = backStack.topRecord?.screen
    LaunchedEffect(currentScreen) {
        if (currentScreen != null) {
            Napier.d(tag = TAG) { "log screen Event $currentScreen" }
            // Log screen transition.
            getKoin()
                .get<PlatformAnalytics>()
                .logScreenEvent(currentScreen)
        }
    }
    CompositionLocalProvider(
        RootNavigator provides navigator,
        LocalPopupController provides PopupController(),
    ) {
        CircuitCompositionLocals(circuit = circuit) {
            BackHandler(enabled = backStack.size > 1) {
                navigator.pop()
            }

            NavigableCircuitContent(navigator, backStack)

            ActionDialog()
        }
    }
}

private fun buildCircuitMobile() =
    buildCircuit(
        presenterFactory =
            listOf(
                RoutePresenterFactory,
            ),
        uiFactory =
            listOf(
                RouteUiFactory,
            ),
    )

@OptIn(ExperimentalCircuitApi::class)
internal fun buildCircuit(
    presenterFactory: List<Presenter.Factory> = emptyList(),
    uiFactory: List<Ui.Factory> = emptyList(),
): Circuit =
    Circuit
        .Builder()
        .addPresenterFactories(presenterFactory)
        .addUiFactories(uiFactory)
        .addAnimatedScreenTransforms(
            transforms =
                mapOf(
                    SearchNestedScreen::class to CustomScreenAnimatedTransform,
                    LibraryNestedScreen::class to CustomScreenAnimatedTransform,
                ),
        ).build()

private fun PlatformAnalytics.logScreenEvent(screen: Screen) {
    logEvent(
        event = "ScreenTransition",
        params =
            mapOf(
                "screen" to screen.toString(),
            ),
    )
}

@OptIn(ExperimentalCircuitApi::class)
object CustomScreenAnimatedTransform : AnimatedScreenTransform {
    override fun AnimatedContentTransitionScope<AnimatedNavState>.enterTransition(animatedNavEvent: AnimatedNavEvent): EnterTransition? {
        // Coming from `HomeScreen` we override the transition to slide in horizontally.
        return if (initialState.screen is SearchNestedScreen || initialState.screen is LibraryNestedScreen) fadeIn() else null
    }

    override fun AnimatedContentTransitionScope<AnimatedNavState>.exitTransition(animatedNavEvent: AnimatedNavEvent): ExitTransition? {
        // Going to `HomeScreen` we override the transition fade out.
        return if (initialState.screen is SearchNestedScreen || initialState.screen is LibraryNestedScreen) fadeOut() else null
    }
}
