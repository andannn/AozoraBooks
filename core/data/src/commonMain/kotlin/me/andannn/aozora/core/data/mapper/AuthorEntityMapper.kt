/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.mapper

import me.andannn.aozora.core.database.entity.AuthorEntity
import me.andannn.aozora.core.domain.model.AuthorData

internal fun AuthorEntity.toModel() =
    AuthorData(
        authorId = authorId,
        birth = birth,
        death = death,
        authorName = "$lastName $firstName",
        authorNameKana = "$lastNameKana $firstNameKana",
        authorNameRomaji = "$lastNameRomaji $firstNameRomaji",
    )
