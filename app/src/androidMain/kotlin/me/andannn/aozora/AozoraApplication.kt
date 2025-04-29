package me.andannn.aozora

import android.app.Application
import com.andannn.aozora.core.service.di.serviceModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module

class AozoraApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }

        startKoin {
            androidContext(this@AozoraApplication)
            modules(
                listOf(
                    *modules.toTypedArray(),
                )
            )
        }
    }
}

val modules: List<Module> = listOf(
    serviceModule,
)