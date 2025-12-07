/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.model

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
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH,
    ) : BaseText()

    /**
     * Ruby element.
     *
     * @property ruby The ruby text.
     */
    data class Ruby(
        override val text: String,
        val ruby: String,
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH,
    ) : BaseText()

    /**
     * Text element with emphasis.
     *
     * @property emphasisStyle the emphasis style.
     */
    data class Emphasis(
        override val text: String,
        val emphasisStyle: EmphasisStyle,
        override val style: AozoraTextStyle = AozoraTextStyle.PARAGRAPH,
    ) : BaseText()

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
        val height: Int?,
    ) : AozoraElement

    /**
     * Indent element.
     *
     * @property count The count of the indent.
     */
    data class Indent(
        /**
         * 表示缩进几个全角字符
         */
        val count: Int,
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

    companion object {
        // When try to divide a element(Such as ruby) which can not divide.
        // Divided left element is assigned to [NoDividedLeftText]
        val NoDividedLeftText = Text("not divided")
    }
}

fun AozoraElement.debugText() =
    when (this) {
        is AozoraElement.BaseText -> {
            text
        }

        is AozoraElement.Illustration -> {
            "[Image $filename]"
        }

        is AozoraElement.Indent -> {
            "[Indent $count]"
        }

        AozoraElement.LineBreak -> {
            "\n"
        }

        AozoraElement.PageBreak -> {
            ""
        }
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
}
