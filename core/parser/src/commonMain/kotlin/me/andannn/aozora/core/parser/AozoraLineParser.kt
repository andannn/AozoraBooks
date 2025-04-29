package me.andannn.aozora.core.parser

import me.andannn.aozora.core.data.common.AozoraElement

/**
 * parse line to block
 */
interface AozoraLineParser {
    fun parseLine(line: RawLine): List<AozoraElement>
}