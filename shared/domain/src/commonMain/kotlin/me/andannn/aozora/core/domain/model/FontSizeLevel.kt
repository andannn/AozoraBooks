/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class FontSizeLevel(
    val fontSizeDp: Dp,
) {
    LEVEL_1(12.dp),
    LEVEL_2(14.dp),
    LEVEL_3(16.dp),
    LEVEL_4(18.dp),
    LEVEL_5(20.dp),
    LEVEL_6(22.dp),
    ;

    companion object {
        val DEFAULT = LEVEL_4
    }
}

fun FontSizeLevel.isLargest(): Boolean = this.ordinal == FontSizeLevel.entries.size - 1

fun FontSizeLevel.isSmallest(): Boolean = this.ordinal == 0

fun FontSizeLevel.next(): FontSizeLevel {
    val nextIndex = (this.ordinal + 1) % FontSizeLevel.entries.size
    return FontSizeLevel.entries[nextIndex]
}

fun FontSizeLevel.pre(): FontSizeLevel {
    val preIndex = (this.ordinal - 1 + FontSizeLevel.entries.size) % FontSizeLevel.entries.size
    return FontSizeLevel.entries[preIndex]
}
