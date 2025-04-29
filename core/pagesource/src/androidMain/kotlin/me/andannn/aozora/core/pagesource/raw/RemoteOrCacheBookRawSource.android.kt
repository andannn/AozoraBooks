package me.andannn.aozora.core.pagesource.raw

import android.content.Context
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.koin.mp.KoinPlatform.getKoin

actual fun getCachedPatchById(id: String): Path {
    return Path(getKoin().get<Context>().filesDir.toString(), "/book/$id").also {
        SystemFileSystem.createDirectories(it)
    }
}
