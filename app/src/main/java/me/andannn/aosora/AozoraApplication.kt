package me.andannn.aosora

import android.app.Application
import android.content.Context

class AozoraApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        private lateinit var INSTANCE: AozoraApplication

        val context: Context
            get() = INSTANCE.applicationContext
    }
}