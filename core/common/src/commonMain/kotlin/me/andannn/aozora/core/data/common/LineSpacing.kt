package me.andannn.aozora.core.data.common

enum class LineSpacing(
    val multiplier: Float,
) {
    SMALL(1.8f),
    MEDIUM(2f),
    LARGE(2.2f),
    ;

    companion object {
        val DEFAULT = MEDIUM
    }
}
