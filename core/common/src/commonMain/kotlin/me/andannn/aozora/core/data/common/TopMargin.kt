/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

enum class TopMargin(
    val value: Float,
) {
    SMALL(10f),
    MEDIUM(30f),
    LARGE(60f),
    ;

    companion object {
        val DEFAULT = MEDIUM
    }
}
