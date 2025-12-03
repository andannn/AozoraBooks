/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.common.screens

import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen

expect object HomeScreen : Screen

expect object LibraryNestedScreen : Screen

expect object SearchNestedScreen : Screen

expect object LicenseScreen : Screen

expect object AboutScreen : Screen

expect class ReaderScreen : Screen {
    constructor(cardId: String, authorId: String)

    val cardId: String
    val authorId: String
}

expect class IndexPageScreen : Screen {
    constructor(kana: String)

    val kana: String
}

expect class AuthorPagesScreen : Screen {
    constructor(code: String)

    val code: String
}

expect class BookCardScreen : Screen {
    constructor(bookCardId: String, groupId: String)

    val bookCardId: String
    val groupId: String
}

expect class AuthorScreen : Screen {
    constructor(authorId: String)

    val authorId: String
}

expect class SearchInputScreen : Screen {
    constructor(initialParam: String?)

    val initialParam: String?
}

expect class SearchInputResult : PopResult {
    constructor(inputText: String)

    val inputText: String
}

expect class SearchResultScreen : Screen {
    constructor(query: String)

    val query: String
}

expect class NdcContentScreen : Screen {
    constructor(ndc: String)

    val ndc: String
}
