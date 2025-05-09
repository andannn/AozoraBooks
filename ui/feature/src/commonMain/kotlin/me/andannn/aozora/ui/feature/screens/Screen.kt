package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.screen.Screen

expect object HomeScreen : Screen

expect class ReaderScreen : Screen {
    constructor(cardId: String)

    val cardId: String
}

expect class IndexPageScreen : Screen {
    constructor(kana: String)

    val kana: String
}

expect class BookCardScreen : Screen {
    constructor(bookCardId: String, groupId: String)

    val bookCardId: String
    val groupId: String
}
