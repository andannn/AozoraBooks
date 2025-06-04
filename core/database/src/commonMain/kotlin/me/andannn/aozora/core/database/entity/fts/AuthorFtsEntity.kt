/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.entity.fts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import me.andannn.aozora.core.database.Tables
import me.andannn.aozora.core.database.entity.AuthorColumns
import me.andannn.aozora.core.database.entity.AuthorEntity

@Fts4(
    contentEntity = AuthorEntity::class,
)
@Entity(tableName = Tables.AUTHOR_FTS_TABLE)
data class AuthorFtsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    val rowId: Int,
    @ColumnInfo(name = AuthorColumns.LAST_NAME)
    val lastName: String,
    @ColumnInfo(name = AuthorColumns.FIRST_NAME)
    val firstName: String,
    @ColumnInfo(name = AuthorColumns.LAST_NAME_KANA)
    val lastNameKana: String?,
    @ColumnInfo(name = AuthorColumns.FIRST_NAME_KANA)
    val firstNameKana: String?,
)
