/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

data class TableOfContentsModel(
    val headingLevel: Int,
    val title: String,
    val lineNumber: Int,
)
