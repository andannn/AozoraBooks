/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.raw

import android.content.Context
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.koin.mp.KoinPlatform.getKoin

actual fun getCachedPatchById(id: String): Path =
    Path(getKoin().get<Context>().filesDir.toString(), "/book/$id").also {
        SystemFileSystem.createDirectories(it)
    }
