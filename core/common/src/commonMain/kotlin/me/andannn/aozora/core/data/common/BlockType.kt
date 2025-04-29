package me.andannn.aozora.core.data.common

sealed interface BlockType {
    /**
     * text base block
     *
     * @property style text style
     * @property indent indent
     */
    sealed class TextType(
        open val style: AozoraTextStyle,
        open val indent: Int
    ) : BlockType

    /**
     * paragraph text block
     */
    data class Text(override val indent: Int = 0) :
        TextType(style = AozoraTextStyle.PARAGRAPH, indent)

    /**
     * heading text block
     */
    data class Heading(
        override val style: AozoraTextStyle,
        override val indent: Int
    ) : TextType(style, indent)

    /**
     * image block
     */
    data object Image : BlockType
}
