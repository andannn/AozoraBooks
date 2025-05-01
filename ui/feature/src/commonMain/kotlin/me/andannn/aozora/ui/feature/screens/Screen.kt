package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.screen.Screen

expect object HomeScreen : Screen

expect class ReaderScreen : Screen {
    constructor(cardId: String)

    val cardId: String
}
