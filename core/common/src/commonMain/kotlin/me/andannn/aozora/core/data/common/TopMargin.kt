/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

enum class TopMargin(
    val value: Float,
) {
    SMALL(80f),
    MEDIUM(120f),
    LARGE(160f),
    ;

    companion object {
        val DEFAULT = MEDIUM
    }
}
