/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.common.screens

import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen

actual data object HomeScreen : Screen

actual object LibraryNestedScreen : Screen

actual object SearchNestedScreen : Screen

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

actual data class AuthorPagesScreen actual constructor(
    actual val code: String,
) : Screen

actual data class AuthorScreen actual constructor(
    actual val authorId: String,
) : Screen

actual data class SearchInputScreen actual constructor(
    actual val initialParam: String?,
) : Screen

actual data class SearchInputResult actual constructor(
    actual val inputText: String,
) : PopResult

actual data class SearchResultScreen actual constructor(
    actual val query: String,
) : Screen
