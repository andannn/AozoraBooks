package me.andannn.aosora.core.common.model

/**
 * Book card from aozora website.
 */
data class AozoraBookCard(
    val id: String,
    val zipUrl: String,
    val htmlUrl: String? = null,
)