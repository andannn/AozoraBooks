/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.platform

enum class Platform {
    IOS,
    ANDROID,
}

expect val appVersion: String

expect val platform: Platform
