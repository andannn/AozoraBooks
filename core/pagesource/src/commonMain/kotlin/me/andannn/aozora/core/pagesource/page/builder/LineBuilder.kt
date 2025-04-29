package me.andannn.aozora.core.pagesource.page.builder

import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.pagesource.measure.ElementMeasureResult
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.Line
import me.andannn.aozora.core.pagesource.util.divide

class LineBuilder(
    private val maxPx: Float,
    initialIndent: Int = 0,
    private val measure: (AozoraElement) -> ElementMeasureResult,
) {
    private var currentHeight: Float = 0f
    private var maxWidth: Float = 0f
    private val elementList = mutableListOf<AozoraElement>()
    private var currentFontStyle: FontStyle? = null

    init {
        if (initialIndent > 0) {
            tryAdd(AozoraElement.Indent(initialIndent))
        }
    }

    fun tryAdd(element: AozoraElement): FillResult {
        when (element) {
            is AozoraElement.Ruby,
            is AozoraElement.Text,
            is AozoraElement.Heading,
            is AozoraElement.Emphasis -> {
                val measureResult = measure(element)
                if (currentHeight + measureResult.size.height > maxPx) {
                    val remainLength = maxPx - currentHeight
                    val singleTextHeight = measureResult.size.height.div(element.length)
                    val remainSlot = remainLength.div(singleTextHeight).toInt()
                    if (remainSlot == 0) {
                        return FillResult.Filled(element)
                    } else {
                        element.divide(remainSlot)?.let {
                            val (left, right) = it
                            val leftResult = measure(left)

                            updateState(left, leftResult)
                            return FillResult.Filled(right)
                        } ?: return FillResult.Filled(element)
                    }
                }

                updateState(element, measureResult)
                return FillResult.FillContinue
            }

            AozoraElement.LineBreak -> {
                val measureResult = measure(element)
                updateState(element, measureResult)
                return FillResult.Filled()
            }

            AozoraElement.PageBreak -> {
                error("Can not handle page break in line")
            }

            is AozoraElement.Illustration,
            is AozoraElement.Indent -> {
                if (elementList.isNotEmpty()) {
                    error("indent, and image can only be add to new line")
                } else {
                    val measureResult = measure(element)
                    updateState(element, measureResult)
                    return FillResult.FillContinue
                }
            }
        }
    }

    fun build(): Line {
        return Line(
            lineHeight = maxWidth,
            elements = elementList.toImmutableList(),
            fontStyle = currentFontStyle
        )
    }

    private fun updateState(element: AozoraElement, measureResult: ElementMeasureResult) {
        elementList += element
        currentHeight += measureResult.size.height
        maxWidth = maxOf(maxWidth, measureResult.size.width)
        if (measureResult.fontStyle != null) {
            currentFontStyle = measureResult.fontStyle
        }
    }
}