/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

enum class FontType {
    NOTO_SANS,
    NOTO_SERIF,
    ;

    companion object {
        // ANDROID Default. No need setting typeface to paint.
        val DEFAULT = NOTO_SANS
    }
}
