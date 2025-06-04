/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.screen.Screen

expect object HomeScreen : Screen

expect object LicenseScreen : Screen

expect object AboutScreen : Screen

expect class ReaderScreen : Screen {
    constructor(cardId: String)

    val cardId: String
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
