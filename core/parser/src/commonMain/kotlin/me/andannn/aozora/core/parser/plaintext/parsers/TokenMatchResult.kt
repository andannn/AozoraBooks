package me.andannn.aozora.core.parser.plaintext.parsers

import me.andannn.aozora.core.parser.plaintext.AozoraPainTextParser

data class TokenMatchResult(
    /**
     * The parser that matched the token.
     */
    val parser: AozoraPainTextParser,

    /** The range of indices in the original string where match was captured. */
    val range: IntRange,

    /**
     * A collection of groups matched by the regular expression.
     *
     * This collection has size of `groupCount + 1` where `groupCount` is the count of groups in the regular expression.
     * Groups are indexed from 1 to `groupCount` and group with the index 0 corresponds to the entire match.
     */
    val groups: List<MatchGroup>,
)

fun MatchResult.toTokenResult(parser: AozoraPainTextParser) = TokenMatchResult(
    parser = parser,
    range = range,
    groups = groups.filterNotNull()
)