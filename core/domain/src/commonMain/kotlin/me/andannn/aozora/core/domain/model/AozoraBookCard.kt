/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

/**
 * Book card from aozora website.
 */
data class AozoraBookCard(
    val id: String,
    val groupId: String,
    val title: String,
    val titleKana: String,
    val author: String?,
    val authorUrl: String?,
    val zipUrl: String?,
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

data class AuthorData(
    val category: String,
    val authorName: String,
    val authorNameKana: String?,
    val authorNameRomaji: String?,
    val birth: String? = null,
    val death: String? = null,
    val authorUrl: String? = null,
    val description: String? = null,
    val descriptionWikiUrl: String? = null,
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
