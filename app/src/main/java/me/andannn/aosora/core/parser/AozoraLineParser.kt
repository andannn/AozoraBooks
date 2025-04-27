package me.andannn.aosora.core.parser

import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.common.util.RawLine

/**
 * parse line to block
 */
interface AozoraLineParser {
    fun parseLine(line: RawLine): List<AozoraElement>
}