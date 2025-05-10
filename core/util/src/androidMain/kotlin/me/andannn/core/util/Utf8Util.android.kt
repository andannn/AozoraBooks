/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.core.util

import io.ktor.utils.io.charsets.decode
import io.ktor.utils.io.charsets.forName
import kotlinx.io.Source

actual fun readStringFromSource(
    source: Source,
    charset: String,
): String = Charsets.forName(charset).newDecoder().decode(source)
