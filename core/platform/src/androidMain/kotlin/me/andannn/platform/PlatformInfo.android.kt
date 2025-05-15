/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.platform

import android.content.Context
import org.koin.mp.KoinPlatform.getKoin

actual val appVersion: String
    get() {
        val context = getKoin().get<Context>()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionName = packageInfo.versionName
        val versionCode = packageInfo.versionCode
        return "$versionName ($versionCode)"
    }
