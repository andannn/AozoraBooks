package me.andannn.aosora.core.parser

sealed interface AozoraElement {

    /**
     * Text element.
     *
     * @property text The text content of the element.
     * @property style The style of the text.
     */
    sealed class BaseText : AozoraElement {
        abstract val text: String
        abstract val style: AozoraTextStyle
    }

    /**
     * Text element.
     */
    data class Text(
        override val text: String,
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH
    ) : BaseText()

    /**
     * Ruby element.
     *
     * @property ruby The ruby text.
     */
    data class Ruby(
        override val text: String,
        val ruby: String,
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH
    ) : BaseText()

    /**
     * Text element with emphasis.
     *
     * @property emphasisStyle the emphasis style.
     */
    data class Emphasis(
        override val text: String,
        val emphasisStyle: EmphasisStyle,
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH
    ) : BaseText()

    /**
     * Heading element.
     *
     * @property style The style of the heading.
     * @property indent The indent of the heading.
     * @property elements The elements of the heading.
     */
    data class Heading(
        override val style: AozoraTextStyle,
        val indent: Int,
        val elements: List<AozoraElement> = emptyList()
    ) : BaseText() {
        override val text: String
            get() = elements.fold("") { acc, element ->
                check(element is BaseText)
                acc + element.text
            }
    }

    /**
     * Illustration element.
     *
     * @property filename The filename of the illustration.
     * @property width The width of the illustration.
     * @property height The height of the illustration.
     */
    data class Illustration(
        val filename: String,
        val width: Int?,
        val height: Int?
    ) : AozoraElement

    /**
     * Indent element.
     *
     * @property count The count of the indent.
     * @property style The style of the indent.
     */
    data class Indent(
        /**
         * 表示缩进几个全角字符
         */
        val count: Int
    ) : AozoraElement

    /**
     * Page break element.
     */
    data object PageBreak : AozoraElement

    /**
     * Line break element.
     */
    data object LineBreak : AozoraElement


    /**
     * length of text
     */
    val length: Int
        get() = (this as? BaseText)?.text?.length ?: (this as? Indent)?.count ?: 0
}

enum class AozoraTextStyle {
    HEADING_LARGE,
    HEADING_MEDIUM,
    HEADING_SMALL,
    PARAGRAPH,
}

enum class EmphasisStyle {
    /**
     * 傍点
     */
    Bouten,

    Strong,
    ;
}