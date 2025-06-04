/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.core.util

fun String.removePrefixRecursive(prefix: String): String {
    var result = this
    while (result.startsWith(prefix)) {
        result = result.removePrefix(prefix)
    }
    return result
}
