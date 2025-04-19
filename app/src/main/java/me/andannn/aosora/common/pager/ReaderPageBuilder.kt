package me.andannn.aosora.common.pager

import androidx.compose.ui.geometry.Size
import me.andannn.aosora.common.parser.AozoraElement
import me.andannn.aosora.common.parser.internal.util.divide


sealed interface FillResult {
    /**
     * Page is full filled and [remainElement] is returned if there is any element is cutoff.
     */
    data class Filled(
        val remainElement: AozoraElement? = null,
    ) : FillResult

    /**
     * Page is not filled. request more elements to fill this page.
     */
    data object FillContinue : FillResult

    /**
     * Can not add the element to page because the new element is an image
     * (Image must be at a single page).
     */
    data object FillReject : FillResult
}

class ReaderPageBuilder(
    meta: ReaderPageMeta,
    val measure: (AozoraElement) -> Size,
    val spacing: () -> Spacing,
) {
    private val fullWidth = meta.renderWidth
    private val fullHeight = meta.renderHeight
    private val lines = mutableListOf<ReaderLine>()

    private var currentWidth: Int = 0
    private var lineBuilder: ReaderLineBuilder? = null

    private var imageContentPager: ReaderPage.ImagePage? = null

    private var isPageBreakAdded = false

    fun tryAdd(element: AozoraElement): FillResult {
        if (isPageBreakAdded) {
            return FillResult.Filled(element)
        }

        if (lineBuilder == null) {
            val size = measure(element)
            if (currentWidth + size.width > fullWidth) {
                return FillResult.Filled(element)
            }
        }

        val builder = lineBuilder ?: ReaderLineBuilder(
            maxPx = fullHeight,
            measure = measure,
            spacing = spacing,
        ).also {
            lineBuilder = it
        }

        when (element) {
            is AozoraElement.Ruby,
            is AozoraElement.Text,
            is AozoraElement.Heading,
            is AozoraElement.LineBreak,
            is AozoraElement.Indent,
            is AozoraElement.Emphasis -> {
                when (val result = builder.tryAdd(element)) {
                    FillResult.FillContinue -> return result
                    FillResult.FillReject -> {
                        error("")
                    }

                    is FillResult.Filled -> {
                        buildNewLine()
                        val remainElement = result.remainElement
                        return if (remainElement == null) {
                            // The element is consumed by new line. return continue
                            FillResult.FillContinue
                        } else {
                            tryAdd(remainElement)
                        }
                    }
                }
            }

            is AozoraElement.Illustration -> {
                if (lines.isNotEmpty() || builder.hasElement()) {
                    // Current page have element, reject.
                    return FillResult.FillReject
                } else {
                    imageContentPager = ReaderPage.ImagePage(
                        imageUri = element.filename,
                        height = element.height ?: 0,
                        width = element.width ?: 0
                    )
                    return FillResult.Filled()
                }
            }

            AozoraElement.PageBreak -> {
                isPageBreakAdded = true
                return FillResult.FillContinue
            }
        }
    }

    fun build(): ReaderPage {
        if (imageContentPager != null) {
            return imageContentPager!!
        }

        if (lineBuilder != null) {
            buildNewLine()
        }

        return ReaderPage.MainContentPage(
            meta = ReaderPageMeta(renderHeight = fullHeight, renderWidth = fullWidth),
            lines = lines
        )
    }

    private fun buildNewLine() {
        val line = lineBuilder!!.build()
        lines += line
        currentWidth += line.lineWidth
        lineBuilder = null
    }
}

class ReaderLineBuilder(
    private val maxPx: Int,
    private val measure: (AozoraElement) -> Size,
    private val spacing: () -> Spacing,
) {
    private var currentHeight: Float = 0f
    private var maxWidth: Float = 0f
    private val elementList = mutableListOf<AozoraElement>()

    fun hasElement(): Boolean {
        return elementList.isNotEmpty()
    }

    fun tryAdd(element: AozoraElement): FillResult {
        when (element) {
            is AozoraElement.Ruby,
            is AozoraElement.Text,
            is AozoraElement.Heading,
            is AozoraElement.Emphasis -> {
                val size = measure(element)
                if (currentHeight + size.height > maxPx) {
                    val remainLength = maxPx - currentHeight
                    val singleTextHeight = size.height.div(element.length)
                    val remainSlot = remainLength.div(singleTextHeight).toInt()
                    if (remainSlot == 0) {
                        return FillResult.Filled(element)
                    } else {
                        element.divide(remainSlot)?.let {
                            val (left, right) = it
                            val leftSize = measure(left)

                            elementList += left
                            currentHeight += leftSize.height
                            maxWidth = maxOf(maxWidth, leftSize.width)
                            return FillResult.Filled(right)
                        } ?: return FillResult.Filled(element)
                    }
                }

                elementList += element
                currentHeight += size.height
                maxWidth = maxOf(maxWidth, size.width)
                return FillResult.FillContinue
            }

            AozoraElement.LineBreak -> {
                val size = measure(element)
                elementList += element
                currentHeight += size.height
                maxWidth = maxOf(maxWidth, size.width)
                return FillResult.Filled()
            }

            AozoraElement.PageBreak -> {
                error("Can not handle page break in line")
            }

            is AozoraElement.Illustration -> {
                error("illustration can not be filled to line")
            }

            is AozoraElement.Indent -> {
                if (elementList.isNotEmpty()) {
                    error("indent can only be add to new line")
                } else {
                    val size = measure(element)
                    elementList += element
                    currentHeight += size.height
                    return FillResult.FillContinue
                }
            }
        }
    }

    fun build(): ReaderLine {
        return ReaderLine(
            spacing = spacing(),
            textWidth = maxWidth.toInt(),
            elements = elementList.toList()
        )
    }
}