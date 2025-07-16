/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import me.andannn.aozora.core.database.Tables

internal object BookIdWithBookCategoryColumns {
    const val BOOK_ID = "book_id"
    const val NDC_MAIN_CLASS_NUM = "ndc_main_class_num"
    const val NDC_DIVISION_NUM = "ndc_division_num"
    const val NDC_SECTION_NUM = "ndc_section_num"
}

@Entity(
    tableName = Tables.BOOK_ID_WITH_BOOK_CATEGORY_TABLE,
    primaryKeys = [
        BookIdWithBookCategoryColumns.BOOK_ID,
        BookIdWithBookCategoryColumns.NDC_MAIN_CLASS_NUM,
        BookIdWithBookCategoryColumns.NDC_DIVISION_NUM,
        BookIdWithBookCategoryColumns.NDC_SECTION_NUM,
    ],
)
data class BookIdWithBookCategory(
    @ColumnInfo(name = BookIdWithBookCategoryColumns.BOOK_ID)
    val bookId: String,
    @ColumnInfo(name = BookIdWithBookCategoryColumns.NDC_MAIN_CLASS_NUM)
    val ndcMainClassNum: Int,
    @ColumnInfo(name = BookIdWithBookCategoryColumns.NDC_DIVISION_NUM)
    val ndcDivisionNum: Int,
    @ColumnInfo(name = BookIdWithBookCategoryColumns.NDC_SECTION_NUM)
    val ndcSectionNum: Int,
)
