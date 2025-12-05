/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.retain.RetainObserver
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

val LocalNavigator: ProvidableCompositionLocal<Navigator> =
    compositionLocalOf { error("no local navigator") }

val RootNavigator: ProvidableCompositionLocal<Navigator> =
    compositionLocalOf { error("no popup controller") }

/**
 * Navigator interface for navigation between screens.
 */
interface Navigator {
    /**
     * Navigate to a new screen.
     *
     * @param screen The screen to navigate to.
     */
    fun goTo(screen: Screen)

    /**
     * Pop the current screen from the back stack.
     */
    fun pop()
}

fun Navigator.popUntil(predicate: (Screen) -> Boolean) {
    (this as? NavigatorImpl) ?: error("navigator is not NavigatorImpl")
    val backStack = backStack ?: return
    while (backStack.lastOrNull() != null && !predicate(backStack.last() as Screen)) {
        pop()
    }
}

class NavigatorImpl :
    RetainObserver,
    Navigator {
    var backStack: NavBackStack<NavKey>? = null

    override fun goTo(screen: Screen) {
        backStack?.add(screen) ?: error("backStack is null")
    }

    override fun pop() {
        with(backStack ?: error("backStack is null")) {
            if (size > 1) {
                removeAt(lastIndex)
            }
        }
    }

    override fun onRetained() {}

    override fun onEnteredComposition() {}

    override fun onExitedComposition() {}

    override fun onRetired() {
        backStack = null
    }

    override fun onUnused() {
        backStack = null
    }
}
