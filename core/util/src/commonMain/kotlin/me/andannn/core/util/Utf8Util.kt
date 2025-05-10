/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.core.util

import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

/**
 * convert string to source.
 */
fun String.asSource(): Source {
    val buffer = Buffer()
    buffer.writeString(this)
    return buffer
}

/**
 * read file as string from path.
 */
fun Path.readString(charset: String): String {
    val source = SystemFileSystem.source(this).buffered()
    return readStringFromSource(source, charset)
}

expect fun readStringFromSource(
    source: Source,
    charset: String,
): String
