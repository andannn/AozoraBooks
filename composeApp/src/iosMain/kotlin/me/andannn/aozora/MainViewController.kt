package me.andannn.aozora

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import me.andannn.aozora.app.AozoraBooksApp
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
// TODO: Check debug build
        Napier.base(DebugAntilog())

        startKoin {
            modules(
                modules
            )
        }
    }
) {
    AozoraBooksApp()
}