package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.screen.Screen

actual data object HomeScreen : Screen

actual data class ReaderScreen actual constructor(
    actual val cardId: String,
) : Screen
