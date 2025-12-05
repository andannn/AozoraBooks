/*
 * Copyright 2025, the Melodify project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.navigation3.runtime.NavEntryDecorator
import me.andannn.aozora.ui.common.dialog.ActionDialog
import me.andannn.aozora.ui.common.dialog.LocalPopupController
import me.andannn.aozora.ui.common.dialog.PopupController

@Composable
fun <T : Any> rememberPopupControllerNavEntryDecorator(): NavEntryDecorator<T> = remember { PopupControllerNavEntryDecorator() }

private class PopupControllerNavEntryDecorator<T : Any> :
    NavEntryDecorator<T>(
        onPop = {
        },
        decorate = { entry ->
            val holder = retain { PopupController() }
            CompositionLocalProvider(
                LocalPopupController provides holder,
            ) {
                entry.Content()

                ActionDialog()
            }
        },
    )
