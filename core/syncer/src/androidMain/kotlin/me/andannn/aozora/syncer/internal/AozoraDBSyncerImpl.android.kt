package me.andannn.aozora.syncer.internal

import android.content.Context
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.koin.mp.KoinPlatform.getKoin

internal actual fun getCachedCsvPath(): Path {
    return Path(getKoin().get<Context>().filesDir.toString(), "/csv").also {
        SystemFileSystem.createDirectories(it)
    }
}