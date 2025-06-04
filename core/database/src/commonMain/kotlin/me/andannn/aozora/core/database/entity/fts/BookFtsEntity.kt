package me.andannn.aozora.core.database.entity.fts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import me.andannn.aozora.core.database.Tables
import me.andannn.aozora.core.database.entity.BookColumns
import me.andannn.aozora.core.database.entity.BookEntity

@Fts4(
    contentEntity = BookEntity::class,
)
@Entity(tableName = Tables.BOOK_FTS_TABLE)
data class BookFtsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    val rowId: Int,
    @ColumnInfo(name = BookColumns.TITLE)
    val title: String,
    @ColumnInfo(name = BookColumns.TITLE_KANA)
    val titleKana: String,
    @ColumnInfo(name = BookColumns.SUBTITLE)
    val subtitle: String?,
    @ColumnInfo(name = BookColumns.SUBTITLE_KANA)
    val subtitleKana: String?,
)
