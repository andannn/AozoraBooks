package me.andannn.aozora.ui.common.navigator

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.slack.circuit.runtime.Navigator

val LocalNavigator: ProvidableCompositionLocal<Navigator> =
    compositionLocalOf { error("no popup controller") }
