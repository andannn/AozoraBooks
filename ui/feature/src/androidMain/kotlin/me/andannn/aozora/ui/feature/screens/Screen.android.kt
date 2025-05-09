package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
actual data object HomeScreen : Screen

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
