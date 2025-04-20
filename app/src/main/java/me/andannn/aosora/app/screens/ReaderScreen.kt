package me.andannn.aosora.app.screens

import androidx.compose.ui.geometry.Size
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.andannn.aosora.ui.reader.Reader
import me.andannn.aosora.ui.reader.ReaderPresenter
import me.andannn.aosora.ui.reader.ReaderState
import me.andannn.aosora.ui.reader.rememberReaderPresenter

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

object ReaderPresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext
    ): Presenter<*>? {
        return when (screen) {
            is ReaderScreen -> ReaderPresenter(
                renderSize = Size(200f, 200f)
            )

            else -> null
        }
    }
}