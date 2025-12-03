/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class TopMargin(
    val value: Dp,
) {
    SMALL(5.dp),
    MEDIUM(10.dp),
    LARGE(15.dp),
    ;

    companion object {
        val DEFAULT = MEDIUM
    }
}
