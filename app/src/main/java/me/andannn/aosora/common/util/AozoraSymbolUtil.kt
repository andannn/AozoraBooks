package me.andannn.aosora.common.util

import me.andannn.aosora.common.model.AozoraString
import me.andannn.aosora.common.util.parser.AozoraTokenParser
import me.andannn.aosora.common.util.parser.SpecificRubyParser

private val parsers = listOf(
    SpecificRubyParser
)

fun parseAozoraLineOptimized(line: String): List<AozoraString> {
    val result = mutableListOf<AozoraString>()
    var index = 0

    while (index < line.length) {
        var bestMatch: MatchResult? = null
        var bestParser: AozoraTokenParser? = null

        for (parser in parsers) {
            val match = parser.match(line, index)
            if (match != null && (bestMatch == null || match.range.first < bestMatch.range.first)) {
                bestMatch = match
                bestParser = parser
            }
        }

        if (bestMatch == null) {
            // 没有任何匹配，直接剩下全是 Text
            result += AozoraString.Text(line.substring(index))
            break
        }

        // 如果最早的匹配不是从 index 开始，就先添加前面的 Text
        if (bestMatch.range.first > index) {
            result += AozoraString.Text(line.substring(index, bestMatch.range.first))
        }

        // 添加匹配结果
        result += bestParser!!.create(bestMatch)
        index = bestMatch.range.last + 1
    }

    return result
}


fun mergeAdjacentText(list: List<AozoraString>): List<AozoraString> {
    val merged = mutableListOf<AozoraString>()

    for (item in list) {
        if (item is AozoraString.Text && merged.lastOrNull() is AozoraString.Text) {
            val last = merged.removeLast() as AozoraString.Text
            merged += AozoraString.Text(last.text + item.text)
        } else {
            merged += item
        }
    }

    return merged
}