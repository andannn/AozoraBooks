package me.andannn.aosora.core.parser

sealed interface AozoraElement {

    sealed class BaseText : AozoraElement {
        abstract val text: String
        abstract val style: AozoraTextStyle
    }

    data class Text(
        override val text: String,
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH
    ) : BaseText()

    data class Ruby(
        override val text: String,
        val ruby: String,
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH
    ) : BaseText()

    data class Emphasis(
        override val text: String,
        val emphasisStyle: EmphasisStyle,
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH
    ) : BaseText()

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

    data class Illustration(
        val filename: String,
        val width: Int?,
        val height: Int?
    ) : AozoraElement

    data class Indent(
        /**
         * 表示缩进几个全角字符
         */
        val count: Int
    ) : AozoraElement

    data object PageBreak : AozoraElement

    data object LineBreak : AozoraElement


    /**
     * length of text
     */
    val length: Int
        get() = (this as? BaseText)?.text?.length ?: 0
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
    Bouten
}