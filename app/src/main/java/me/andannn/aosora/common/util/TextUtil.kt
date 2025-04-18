package me.andannn.aosora.common.util

fun Char.isHiragana(): Boolean {
    return this in '\u3040'..'\u309F'
}

fun Char.isKatakana(): Boolean {
    return this in '\u30A0'..'\u30FF' || this in '\u31F0'..'\u31FF' || this in '\uFF65'..'\uFF9F'
}

fun Char.isKana(): Boolean = isHiragana() || isKatakana()