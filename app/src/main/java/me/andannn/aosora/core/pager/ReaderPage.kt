package me.andannn.aosora.core.pager

import kotlinx.collections.immutable.ImmutableList
import me.andannn.aosora.core.common.FontStyle
import me.andannn.aosora.core.common.PageMetaData
import me.andannn.aosora.core.parser.AozoraElement

/**
 * Page of reader.
 */
data class AozoraPage(
    val lines: ImmutableList<ReaderLine>
)

val AozoraPage.fullText: String
    get() = lines.fold("") { acc, line ->
        acc + line.fullText
    }

data class ReaderLine constructor(
    /**
     * 每一行的实际高度（像素）。
     * 常见设置：行高 = fontSize × 行距倍数（如 1.5）
     *
     * 例如：
     * fontSize = 16
     * lineHeight = 24  (即 16 × 1.5)
     */
    val lineHeight: Float,

    /**
     * font style.
     */
    val fontStyle: FontStyle?,

    /**
     * elements in line.
     */
    val elements: ImmutableList<AozoraElement>,
) {
    val fullText: String
            by lazy {
                elements.fold("") { acc, element ->
                    acc + when (element) {
                        is AozoraElement.BaseText -> {
                            element.text
                        }
                        is AozoraElement.Illustration -> "[Image ${element.filename}]"
                        is AozoraElement.Indent -> "[Indent ${element.count}]"
                        AozoraElement.LineBreak -> "\n"
                        AozoraElement.PageBreak -> ""
                    }
                }
            }
}