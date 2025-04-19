package me.andannn.aosora.common.pager

import me.andannn.aosora.common.parser.AozoraElement

data class ReaderPageMeta(
    val renderHeight: Int,
    val renderWidth: Int,
)

/**
 * Page of reader.
 */
sealed interface ReaderPage {
    /**
     * Cover page.
     */
    data object CoverPage : ReaderPage

    /**
     * Image is placed at a single page.
     */
    data class MainContentPage(
        val meta: ReaderPageMeta,
        val lines: List<ReaderLine>
    ) : ReaderPage

    /**
     * Image is placed at a single page.
     */
    data class ImagePage(
        val imageUri: String,
        val height: Int,
        val width: Int
    ) : ReaderPage
}

data class Spacing(
    /**
     * spacing in pixel before line.
     */
    val lineSpacingBefore: Int,

    /**
     * spacing in pixel after line.
     */
    val lineSpacingAfter: Int,
)

data class ReaderLine(
    /**
     * spacing in pixel.
     */
    val spacing: Spacing,

    /**
     * width of line in pixel.
     */
    val textWidth: Int,

    /**
     * elements in line.
     */
    val elements: List<AozoraElement>,
) {
    val lineWidth: Int
        get() = spacing.lineSpacingBefore + textWidth + spacing.lineSpacingAfter

    val fullText: String
            by lazy {
                elements.fold("") { acc, element ->
                    acc + ((element as? AozoraElement.BaseText)?.text ?: "")
                }
            }
}