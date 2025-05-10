/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser

import me.andannn.aozora.core.data.common.AozoraElement

/**
 * parse line to block
 */
interface AozoraLineParser {
    fun parseLine(line: RawLine): List<AozoraElement>
}
