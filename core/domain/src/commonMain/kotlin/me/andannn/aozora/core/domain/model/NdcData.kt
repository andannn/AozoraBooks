/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

import kotlin.jvm.JvmInline

data class NdcData(
    val ndcClassification: NDCClassification,
    val label: String,
)

enum class NDCType {
    MAIN_CLASS,
    DIVISION,
    SECTION,
}

/**
 * NDC classification class
 *
 * https://www.jla.or.jp/committees/bunrui/ndc-data/
 */
@JvmInline
value class NDCClassification(
    val value: String,
) {
    init {
        require(value.isNotEmpty()) { "NDC classification cannot be empty" }
        require(value.all { it.isDigit() }) { "NDC classification must contain only digits. $value" }
        require(value.length <= 3) { "NDC classification must be at most 3 characters long $value" }
    }

    val mainClassNum: Int
        get() = value.substring(0, 1).toInt()

    val divisionNum: Int?
        get() = value.takeIf { it.length >= 2 }?.substring(1, 2)?.toInt()

    val sectionNum: Int?
        get() = value.takeIf { it.length >= 3 }?.substring(2, 3)?.toInt()

    val ndcType: NDCType
        get() =
            when (value.length) {
                1 -> NDCType.MAIN_CLASS
                2 -> NDCType.DIVISION
                3 -> NDCType.SECTION
                else -> throw IllegalArgumentException("NDC classification must be 1 to 3 characters long")
            }

    override fun toString(): String = "NDC $value"
}

fun NDCClassification.isChildOf(other: NDCClassification): Boolean =
    when (this.ndcType) {
        NDCType.MAIN_CLASS -> false
        NDCType.DIVISION ->
            other.ndcType == NDCType.MAIN_CLASS &&
                this.mainClassNum == other.mainClassNum

        NDCType.SECTION ->
            other.ndcType == NDCType.DIVISION &&
                this.mainClassNum == other.mainClassNum &&
                this.divisionNum == other.divisionNum
    }
