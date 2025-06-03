package me.andannn.aozora.core.database.embedded

import androidx.room.Embedded
import androidx.room.Relation
import me.andannn.aozora.core.database.entity.AuthorColumns
import me.andannn.aozora.core.database.entity.AuthorEntity
import me.andannn.aozora.core.database.entity.BookColumns
import me.andannn.aozora.core.database.entity.BookEntity

data class AuthorWithBooks(
    @Embedded val author: AuthorEntity,
    @Relation(
        parentColumn = AuthorColumns.AUTHOR_ID,
        entityColumn = BookColumns.AUTHOR_ID,
    )
    val books: List<BookEntity>,
)
