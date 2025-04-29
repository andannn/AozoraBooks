package me.andannn.aozora.core.data.common

/**
 * Book card from aozora website.
 */
data class AozoraBookCard(
    val id: String,
    val zipUrl: String,
    val htmlUrl: String? = null,
)