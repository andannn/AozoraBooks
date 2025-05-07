package me.andannn.aozora.core.datastore.di

import android.content.Context
import kotlinx.io.files.Path
import org.koin.mp.KoinPlatform.getKoin

actual val fileDir: Path
    get() = Path(getKoin().get<Context>().filesDir.toString())
