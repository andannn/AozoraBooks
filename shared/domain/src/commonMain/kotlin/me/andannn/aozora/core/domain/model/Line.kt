/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import androidx.compose.ui.unit.Dp
import kotlinx.collections.immutable.ImmutableList

data class Line(
    /**
     * 每一行的实际高度（Dp）。
     * 常见设置：行高 = fontSize × 行距倍数（如 1.5）
     *
     * 例如：
     * fontSize = 16
     * lineHeight = 24  (即 16 × 1.5)
     */
    val lineHeight: Dp,
    /**
     * elements in line.
     */
    val elements: ImmutableList<ElementDetail>,
)

data class ElementDetail(
    val element: AozoraElement,
    val fontStyle: FontStyle? = null,
)
