package me.andannn.aozora.core.data.common

enum class FontSizeLevel(
    val fontSize: Float
) {
    Level_1(30f),
    Level_2(36f),
    Level_3(42f),
    Level_4(48f),
    Level_5(54f),
    Level_6(60f),
    ;
}

fun FontSizeLevel.next(): FontSizeLevel {
    val nextIndex = (this.ordinal + 1) % FontSizeLevel.entries.size
    return FontSizeLevel.entries[nextIndex]
}