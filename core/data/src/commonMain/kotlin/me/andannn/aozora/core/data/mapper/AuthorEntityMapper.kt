/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.mapper

import me.andannn.aozora.core.database.entity.AuthorEntity
import me.andannn.aozora.core.domain.model.AuthorModel

internal fun AuthorEntity.toModel() =
    AuthorModel(
        authorId = authorId,
        lastName = lastName,
        firstName = firstName,
        lastNameKana = lastNameKana,
        firstNameKana = firstNameKana,
        lastNameSortKana = lastNameSortKana,
        firstNameSortKana = firstNameSortKana,
        lastNameRomaji = lastNameRomaji,
        firstNameRomaji = firstNameRomaji,
        birth = birth,
        death = death,
        copyrightFlag = copyrightFlag,
    )
