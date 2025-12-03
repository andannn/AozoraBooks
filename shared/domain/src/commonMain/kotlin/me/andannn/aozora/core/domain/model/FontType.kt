/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

enum class FontType {
    NOTO_SERIF,
    NOTO_SANS,
    ;

    companion object {
        val DEFAULT = NOTO_SERIF
    }
}
