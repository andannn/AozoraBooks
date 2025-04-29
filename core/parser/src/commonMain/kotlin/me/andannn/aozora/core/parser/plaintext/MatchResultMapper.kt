package me.andannn.aozora.core.parser.plaintext

import me.andannn.aozora.core.parser.plaintext.parsers.TokenMatchResult

/**
 * group 1 of [MatchResult] will contains extra text before target, cut the start of the range and return.
 */
fun MatchResult.mapTokenMatchResultWitCutStart(
    input: String,
    parser: AozoraPainTextParser
): TokenMatchResult? {
    val match = this
    val base = match.groupValues[1]
    val target = match.groupValues[2]
// TODO: Implement this in Common module.
//    if (base.endsWith(target)) {
//        val start = match.groups[1]!!.range.last - target.length + 1
//        val end = match.range.last
//        val range = start..end
//        val groups = match.groups.filterNotNull()
//        return TokenMatchResult(
//            parser = parser,
//            range = range,
//            groups = groups.toMutableList().apply {
//                this[0] = MatchGroup(input.substring(range), range)
//                this[1] = MatchGroup(input.substring(range), range)
//            }
//        )
//    } else {
        return null
//    }
}