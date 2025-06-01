/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal.util

import kotlinx.io.Source
import kotlinx.io.readLine
import me.andannn.aozora.syncer.internal.model.BookModel

internal fun Source.parseAsBookModel(): Sequence<BookModel> =
    sequence {
        this@parseAsBookModel.asCsvDataSequence().forEach {
            yield(BookModel(it))
        }
    }

private fun Source.asCsvDataSequence(): Sequence<List<String>> =
    sequence {
        var isFirstLine = true
        while (!exhausted()) {
            if (isFirstLine) {
                // ignore first line.
                this@asCsvDataSequence.readLine()
                isFirstLine = false
                continue
            }

            val line = this@asCsvDataSequence.readLine() ?: continue
            yield(line.split(",").map { it.trim('"') })
        }
    }
