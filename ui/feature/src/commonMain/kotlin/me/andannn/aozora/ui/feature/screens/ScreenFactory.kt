package me.andannn.aozora.ui.feature.screens

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.andannn.aozora.ui.feature.home.Home
import me.andannn.aozora.ui.feature.home.HomePresenter
import me.andannn.aozora.ui.feature.home.HomeState
import me.andannn.aozora.ui.feature.reader.Reader
import me.andannn.aozora.ui.feature.reader.ReaderPresenter
import me.andannn.aozora.ui.feature.reader.ReaderState

object RouteUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is HomeScreen -> ui<HomeState> { state, modifier ->
                Home(state, modifier)
            }

            is ReaderScreen -> ui<ReaderState> { state, modifier ->
                Reader(state, modifier)
            }

            else -> null
        }
    }
}

object RoutePresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext
    ): Presenter<*>? {
        return when (screen) {
            is HomeScreen -> HomePresenter(navigator)
            is ReaderScreen -> ReaderPresenter(screen.cardId)
            else -> null
        }
    }
}