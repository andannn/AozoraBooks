package me.andannn.aosora.app.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen

@Parcelize
data class ReaderScreen(val cardId: String) : Screen