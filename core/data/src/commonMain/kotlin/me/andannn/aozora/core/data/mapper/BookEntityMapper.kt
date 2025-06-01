package me.andannn.aozora.core.data.mapper

import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.domain.model.AozoraBookCard

internal fun BookEntity.toModel() =
    AozoraBookCard(
        id = bookId,
        groupId = authorId,
        title = title,
        titleKana = titleKana,
        author = authorFirstName + authorLastName,
        zipUrl = textFileUrl,
        htmlUrl = htmlFileUrl,
        authorDataList = emptyList(),
        authorUrl = null,
    )
