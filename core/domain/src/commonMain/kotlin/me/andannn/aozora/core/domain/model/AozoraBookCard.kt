/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

/**
 * Book card from aozora website.
 */
data class AozoraBookCard constructor(
    val id: String,
    val title: String,
    val titleKana: String,
    val authorId: String,
    val author: String?,
    val authorUrl: String?,
    val zipUrl: String?,
    val subTitle: String? = null,
    val htmlUrl: String? = null,
    val category: String? = null,
    val source: String? = null,
    val characterType: String? = null,
    val staffData: StaffData? = null,
    val authorDataList: List<AuthorData>,
)

data class StaffData(
    val proofreading: String?,
    val input: String?,
)

data class TitleItem(
    val title: String,
    val subTitle: String?,
    val link: String,
)

data class BookColumnItem(
    val index: String,
    val title: TitleItem,
    val characterCategory: String,
    val author: String,
    val translator: String?,
)
