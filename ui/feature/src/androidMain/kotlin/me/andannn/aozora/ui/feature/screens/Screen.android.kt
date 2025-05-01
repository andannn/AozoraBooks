package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
actual data object HomeScreen : Screen

@Parcelize
actual data class ReaderScreen actual constructor(
    actual val cardId: String,
) : Screen
