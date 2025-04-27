package me.andannn.aosora.app.screens

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.andannn.aosora.ui.reader.Reader
import me.andannn.aosora.ui.reader.ReaderState

object ReaderUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ReaderScreen -> ui<ReaderState> { state, modifier ->
                Reader(state, modifier)
            }

            else -> null
        }
    }
}
