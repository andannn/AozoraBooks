package me.andannn.aosora.core.parser

import me.andannn.aosora.core.common.model.AozoraElement

/**
 * parse line to block
 */
interface AozoraLineParser {
    fun parseLine(line: String): List<AozoraElement>
}