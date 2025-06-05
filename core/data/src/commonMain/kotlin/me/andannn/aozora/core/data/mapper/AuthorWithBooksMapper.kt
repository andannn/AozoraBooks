/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.mapper

import me.andannn.aozora.core.domain.model.AuthorWithBooks

fun me.andannn.aozora.core.database.embedded.AuthorWithBooks.toModel(): AuthorWithBooks =
    AuthorWithBooks(
        author = author.toModel(),
        books = books.map { it.toModel() },
    )
