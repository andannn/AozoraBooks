/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.mapper

import me.andannn.aozora.core.database.entity.BookEntity
import me.andannn.aozora.core.domain.model.AozoraBookCard
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.model.StaffData
import me.andannn.aozora.core.domain.model.asNDCClassification

internal fun BookEntity.toModel() =
    AozoraBookCard(
        id = bookId,
        authorId = authorId,
        title = title,
        titleKana = titleKana,
        author = "$authorLastName $authorFirstName",
        zipUrl = textFileUrl,
        htmlUrl = htmlFileUrl,
        authorUrl = null,
        categories = categoryNo?.asNDCClassification() ?: emptyList(),
        source = firstAppearance,
        characterType = orthography,
        haveCopyRight = this.workCopyrightFlag == "あり",
        staffData =
            StaffData(
                input = inputBy,
                proofreading = proofBy,
            ),
        subTitle = subtitle,
        cardUrl = cardUrl,
        authorDataList =
            listOf(
                AuthorData(
                    authorId = authorId,
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
