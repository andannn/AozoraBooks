package me.andannn.aozora.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.andannn.aozora.core.database.Tables.SAVED_BOOK_TABLE

internal object SavedBookColumns {
    const val BOOK_ID = "book_id"
    const val GROUP_ID = "group_id"
    const val TITLE = "title"
    const val TITLE_KANA = "title_kana"
    const val AUTHOR = "author"
    const val AUTHOR_URL = "author_url"
    const val ZIP_URL = "zip_url"
    const val HTML_URL = "html_url"
    const val SAVED_DATE = "saved_date"
}

@Entity(tableName = SAVED_BOOK_TABLE)
data class SavedBookEntity(
    @PrimaryKey
    @ColumnInfo(name = SavedBookColumns.BOOK_ID)
    val bookId: String,
    @ColumnInfo(name = SavedBookColumns.GROUP_ID)
    val groupId: String,
    @ColumnInfo(name = SavedBookColumns.TITLE)
    val title: String,
    @ColumnInfo(name = SavedBookColumns.TITLE_KANA)
    val titleKana: String,
    @ColumnInfo(name = SavedBookColumns.AUTHOR)
    val author: String,
    @ColumnInfo(name = SavedBookColumns.AUTHOR_URL)
    val authorUrl: String,
    @ColumnInfo(name = SavedBookColumns.ZIP_URL)
    val zipUrl: String,
    @ColumnInfo(name = SavedBookColumns.HTML_URL)
    val htmlUrl: String,
    @ColumnInfo(name = SavedBookColumns.SAVED_DATE)
    val savedDate: Long,
)
