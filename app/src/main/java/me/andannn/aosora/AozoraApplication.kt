package me.andannn.aosora

import android.app.Application
import android.content.Context
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class AozoraApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
//        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
//        }
    }

    companion object {
        private lateinit var INSTANCE: AozoraApplication

        val context: Context
            get() = INSTANCE.applicationContext
    }
}