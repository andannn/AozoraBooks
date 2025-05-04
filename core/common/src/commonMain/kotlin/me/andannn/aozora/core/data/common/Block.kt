package me.andannn.aozora.core.data.common

sealed class Block(
    open val blockIndex: Int,
    open val elements: List<AozoraElement>,
    open val byteRange: LongRange,
) {
    sealed class TextBlock(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        override val byteRange: LongRange,
        open val textStyle: AozoraTextStyle,
        open val indent: Int = 0,
    ) : Block(blockIndex, elements, byteRange)

    data class Heading(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        override val byteRange: LongRange,
        override val textStyle: AozoraTextStyle,
        override val indent: Int,
    ) : TextBlock(blockIndex, elements, byteRange, textStyle)

    data class Paragraph(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        override val byteRange: LongRange,
    ) : TextBlock(blockIndex, elements, byteRange, AozoraTextStyle.PARAGRAPH, 0)

    data class Image(
        override val blockIndex: Int,
        override val elements: List<AozoraElement>,
        override val byteRange: LongRange,
    ) : Block(blockIndex, elements, byteRange)

    val textCount: Int by lazy {
        elements.fold(0) { acc, element ->
            acc + element.length
        }
    }

    val fullText: String by lazy {
        elements.fold("") { acc, element ->
            acc + element.debugText()
        }
    }

    fun copyWith(elements: List<AozoraElement>): Block =
        when (this) {
            is Heading -> this.copy(elements = elements)
            is Image -> this.copy(elements = elements)
            is Paragraph -> this.copy(elements = elements)
        }
}
