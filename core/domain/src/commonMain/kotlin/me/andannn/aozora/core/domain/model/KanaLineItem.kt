package me.andannn.aozora.core.domain.model

enum class KanaLineItem(
    val code: String,
    val kanaLabel: String,
    val kanaList: List<String>,
) {
    KanaLineItemA("a", "あ行", listOf("あ", "い", "う", "え", "お")),
    KanaLineItemKA("ka", "か行", listOf("か", "き", "く", "け", "こ")),
    KanaLineItemSA("sa", "さ行", listOf("さ", "し", "す", "せ", "そ")),
    KanaLineItemNA("na", "な行", listOf("な", "に", "ぬ", "ね", "の")),
    KanaLineItemTA("ta", "た行", listOf("た", "ち", "つ", "て", "と")),
    KanaLineItemHA("ha", "は行", listOf("は", "ひ", "ふ", "へ", "ほ")),
    KanaLineItemMA("ma", "ま行", listOf("ま", "み", "む", "め", "も")),
    KanaLineItemRA("ra", "ら行", listOf("ら", "り", "る", "れ", "ろ")),
    KanaLineItemYA("ya", "や行", listOf("や", "ゆ", "よ")),
    KanaLineItemWA("wa", "わ行", listOf("わ", "を")),
    ;

    companion object {
        fun fromCode(code: String): KanaLineItem? = entries.find { it.code == code }
    }
}
