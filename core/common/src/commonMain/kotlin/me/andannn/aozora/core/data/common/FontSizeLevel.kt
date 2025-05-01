package me.andannn.aozora.core.data.common

enum class FontSizeLevel(
    val fontSize: Float,
) {
    LEVEL_1(30f),
    LEVEL_2(36f),
    LEVEL_3(42f),
    LEVEL_4(48f),
    LEVEL_5(54f),
    LEVEL_6(60f),
}

fun FontSizeLevel.next(): FontSizeLevel {
    val nextIndex = (this.ordinal + 1) % FontSizeLevel.entries.size
    return FontSizeLevel.entries[nextIndex]
}
