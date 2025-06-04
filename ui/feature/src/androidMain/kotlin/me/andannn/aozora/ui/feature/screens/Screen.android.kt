/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
actual data object HomeScreen : Screen

@Parcelize
actual data object LicenseScreen : Screen

@Parcelize
actual data object AboutScreen : Screen

@Parcelize
actual data class ReaderScreen actual constructor(
    actual val cardId: String,
) : Screen

@Parcelize
actual data class IndexPageScreen actual constructor(
    actual val kana: String,
) : Screen

@Parcelize
actual data class BookCardScreen actual constructor(
    actual val bookCardId: String,
    actual val groupId: String,
) : Screen

@Parcelize
actual data class AuthorPagesScreen actual constructor(
    actual val code: String,
) : Screen

@Parcelize
actual data class AuthorScreen actual constructor(
    actual val authorId: String,
) : Screen
