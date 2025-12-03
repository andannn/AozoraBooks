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
            yield(parseCsvLine(line))
        }
    }

private fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
    var inQuotes = false
    var i = 0

    while (i < line.length) {
        val c = line[i]
        when {
            c == '"' -> {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    // Double quote inside quoted field → interpreted as one quote
                    current.append('"')
                    i++ // skip next quote
                } else {
                    inQuotes = !inQuotes
                }
            }

            c == ',' && !inQuotes -> {
                result.add(current.toString())
                current = StringBuilder()
            }

            else -> {
                current.append(c)
            }
        }
        i++
    }
    result.add(current.toString())
    return result
}
