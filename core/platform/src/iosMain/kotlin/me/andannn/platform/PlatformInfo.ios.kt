/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.platform

import platform.Foundation.NSBundle

actual val appVersion: String
    get() {
        val versionString = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString")
        val versionCode = NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion")
        return "$versionString ($versionCode)"
    }
actual val platform: Platform = Platform.IOS
