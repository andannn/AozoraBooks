/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.screen.Screen

actual data object HomeScreen : Screen

actual data object LicenseScreen : Screen

actual data object AboutScreen : Screen

actual data class ReaderScreen actual constructor(
    actual val cardId: String,
) : Screen

actual data class IndexPageScreen actual constructor(
    actual val kana: String,
) : Screen

actual data class BookCardScreen actual constructor(
    actual val bookCardId: String,
    actual val groupId: String,
) : Screen
