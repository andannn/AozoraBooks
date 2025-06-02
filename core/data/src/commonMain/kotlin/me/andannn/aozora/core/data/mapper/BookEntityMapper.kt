/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.mapper

import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.model.StaffData

internal fun BookEntity.toModel() =
    AozoraBookCard(
        id = bookId,
        groupId = authorId,
        title = title,
        titleKana = titleKana,
        author = "$authorLastName $authorFirstName",
        zipUrl = textFileUrl,
        htmlUrl = htmlFileUrl,
        authorUrl = null,
        category = categoryNo,
        source = firstAppearance,
        characterType = orthography,
        staffData =
            StaffData(
                input = inputBy,
                proofreading = proofBy,
            ),
        authorDataList =
            listOf(
                AuthorData(
                    category = authorRoleFlag ?: "",
                    authorName = "$authorLastName $authorFirstName",
                    authorNameKana = "$authorLastNameSortKana $authorFirstNameKana",
                    authorNameRomaji = "$authorLastNameRomaji, $authorFirstNameRomaji",
                    birth = authorBirth,
                    death = authorDeath,
                    authorUrl = null,
                    description = null,
                    descriptionWikiUrl = null,
                ),
            ),
    )
