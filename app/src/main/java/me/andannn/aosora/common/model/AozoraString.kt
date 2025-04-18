package me.andannn.aosora.common.model

import me.andannn.aosora.common.util.parser.AozoraTokenParser

sealed class AozoraString(
    open val text: String
) {
    data class Text(override val text: String) : AozoraString(text)
    data class Ruby(override val text: String, val ruby: String) : AozoraString(text)
}
