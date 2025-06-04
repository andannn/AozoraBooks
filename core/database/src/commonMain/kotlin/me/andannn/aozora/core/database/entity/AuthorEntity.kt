/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.andannn.aozora.core.database.Tables

object AuthorColumns {
    const val AUTHOR_ID = "author_id"
    const val LAST_NAME = "last_name"
    const val FIRST_NAME = "first_name"
    const val LAST_NAME_KANA = "last_name_kana"
    const val FIRST_NAME_KANA = "first_name_kana"
    const val LAST_NAME_SORT_KANA = "last_name_sort_kana"
    const val FIRST_NAME_SORT_KANA = "first_name_sort_kana"
    const val LAST_NAME_ROMAJI = "last_name_romaji"
    const val FIRST_NAME_ROMAJI = "first_name_romaji"
    const val BIRTH = "birth"
    const val DEATH = "death"
    const val COPYRIGHT_FLAG = "copyright_flag"
}

@Entity(tableName = Tables.AUTHOR_TABLE)
data class AuthorEntity(
    @PrimaryKey
    @ColumnInfo(name = AuthorColumns.AUTHOR_ID)
    val authorId: String,
    @ColumnInfo(name = AuthorColumns.LAST_NAME)
    val lastName: String,
    @ColumnInfo(name = AuthorColumns.FIRST_NAME)
    val firstName: String,
    @ColumnInfo(name = AuthorColumns.LAST_NAME_KANA)
    val lastNameKana: String?,
    @ColumnInfo(name = AuthorColumns.FIRST_NAME_KANA)
    val firstNameKana: String?,
    @ColumnInfo(name = AuthorColumns.LAST_NAME_SORT_KANA)
    val lastNameSortKana: String?,
    @ColumnInfo(name = AuthorColumns.FIRST_NAME_SORT_KANA)
    val firstNameSortKana: String?,
    @ColumnInfo(name = AuthorColumns.LAST_NAME_ROMAJI)
    val lastNameRomaji: String?,
    @ColumnInfo(name = AuthorColumns.FIRST_NAME_ROMAJI)
    val firstNameRomaji: String?,
    @ColumnInfo(name = AuthorColumns.BIRTH)
    val birth: String?,
    @ColumnInfo(name = AuthorColumns.DEATH)
    val death: String?,
    @ColumnInfo(name = AuthorColumns.COPYRIGHT_FLAG)
    val copyrightFlag: String?,
)
