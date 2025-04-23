package me.andannn.aosora.core.parser

/**
 * parse line to block
 */
interface AozoraLineParser {
    fun parseLine(line: String): List<AozoraElement>
}