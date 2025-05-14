/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.common

data class BookModelTemp constructor(
    val id: String,
    val groupId: String,
    val title: String,
    val titleKana: String,
    val authorName: String?,
    val zipUrl: String?,
    val htmlUrl: String?,
)
