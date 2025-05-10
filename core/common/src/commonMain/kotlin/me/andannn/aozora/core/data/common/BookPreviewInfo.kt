/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

data class BookPreviewInfo(
    val id: String,
    val title: String,
    val authorName: String?,
    val zipUrl: String,
    val htmlUrl: String,
    val bookCardUrl: String,
)
