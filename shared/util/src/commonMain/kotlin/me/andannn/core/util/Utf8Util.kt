/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.core.util

import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * read file as string from path.
 */
fun Path.readString(charset: String): String {
    val source = SystemFileSystem.source(this).buffered()
    return readStringFromSource(source, charset)
}

internal expect fun readStringFromSource(
    source: Source,
    charset: String,
): String
