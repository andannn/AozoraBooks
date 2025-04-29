package me.andannn.aosora.app

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.andannn.aosora.app.screens.HomeScreen
import me.andannn.aosora.app.screens.RoutePresenterFactory
import me.andannn.aosora.app.screens.RouteUiFactory

@Composable
fun AozoraBooksApp(
    modifier: Modifier = Modifier,
    circuit: Circuit = buildCircuitMobile(),
) {
    CircuitCompositionLocals(circuit = circuit) {
        val backStack = rememberSaveableBackStack(HomeScreen)
        val navigator = rememberCircuitNavigator(backStack) {

        }

        BackHandler(enabled = backStack.size > 1) {
            navigator.pop()
        }

        NavigableCircuitContent(navigator, backStack)
    }
}

private fun buildCircuitMobile() = buildCircuit(
    presenterFactory = listOf(
        RoutePresenterFactory,
    ),
    uiFactory = listOf(
        RouteUiFactory,
    )
)

internal fun buildCircuit(
    presenterFactory: List<Presenter.Factory> = emptyList(),
    uiFactory: List<Ui.Factory> = emptyList(),
): Circuit {
    return Circuit.Builder()
        .addPresenterFactories(presenterFactory)
        .addUiFactories(uiFactory)
        .build()
}