package me.andannn.aozora.core.data.common

/**
 * Book card from aozora website.
 */
data class AozoraBookCard(
    val id: String,
    val title: String,
    val titleKana: String,
    val author: String,
    val zipUrl: String,
    val htmlUrl: String? = null,
    val category: String? = null,
    val source: String? = null,
    val characterType: String? = null,
    val staffData: StaffData? = null,
    val authorData: AuthorData,
)

data class StaffData(
    val proofreading: String,
    val input: String,
)

data class AuthorData(
    val authorId: String,
    val authorPageUrl: String,
    val authorName: String,
    val authorNameKana: String,
    val birthDay: String? = null,
    val deathDay: String? = null,
    val externalLink: String? = null,
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
)
