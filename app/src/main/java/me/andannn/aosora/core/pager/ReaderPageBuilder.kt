package me.andannn.aosora.core.pager

import androidx.compose.ui.geometry.Size
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aosora.core.common.FontStyle
import me.andannn.aosora.core.measure.DefaultElementMeasurer
import me.andannn.aosora.core.measure.ElementMeasurer
import me.andannn.aosora.core.measure.MeasureResult
import me.andannn.aosora.core.measure.PageMetaData
import me.andannn.aosora.core.parser.AozoraBlock
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.BlockType
import me.andannn.aosora.core.parser.internal.util.divide
import org.jetbrains.annotations.VisibleForTesting

private const val TAG = "ReaderPageBuilder"

sealed interface FillResult {
    data class Filled(
        val remainElement: AozoraElement? = null,
        val remainBlock: AozoraBlock? = null,
    ) : FillResult

    data object FillContinue : FillResult
}

fun createDefaultReaderPageBuilder(
    meta: PageMetaData,
) = ReaderPageBuilder(
    fullWidth = meta.renderWidth,
    fullHeight = meta.renderHeight,
    measurer = DefaultElementMeasurer(meta),
)

class ReaderPageBuilder(
    private val fullWidth: Float,
    private val fullHeight: Float,
    private val measurer: ElementMeasurer,
) {
    private val lines = mutableListOf<ReaderLine>()

    private var currentWidth: Float = 0f
    private var lineBuilder: ReaderLineBuilder? = null

    private var isPageBreakAdded = false

    fun tryAddBlock(block: AozoraBlock): FillResult {
        val remainingElements = block.elements.toMutableList()

        while (remainingElements.isNotEmpty()) {
            val element = remainingElements.first()
            val result = tryAddElement(
                element,
                lineIndent = (block.blockType as? BlockType.TextType)?.indent ?: 0,
                sizeOf = { element ->
                    measurer.measure(
                        element,
                        (block.blockType as? BlockType.TextType)?.style
                    )
                }
            )

            when (result) {
                is FillResult.FillContinue -> {
                    remainingElements.removeAt(0) // 成功消费，移除
                }

                is FillResult.Filled -> {
                    remainingElements.removeAt(0) // 继续消费本 element
                    result.remainElement?.let { remainingElements.add(0, it) } // 还原未消费部分
                    break
                }
            }
        }

        return if (remainingElements.isEmpty()) {
            FillResult.FillContinue
        } else {
            FillResult.Filled(remainBlock = block.copy(remainingElements))
        }
    }

    @VisibleForTesting
    fun tryAddElement(
        element: AozoraElement,
        lineIndent: Int,
        sizeOf: (AozoraElement) -> MeasureResult
    ): FillResult {
        Napier.d(tag = TAG) { "tryAddElement E. element $element" }
        if (isPageBreakAdded) {
            return FillResult.Filled(element)
        }

        if (element is AozoraElement.PageBreak) {
            isPageBreakAdded = true
            return FillResult.FillContinue
        }

        if (lineBuilder == null) {
            val measureResult = sizeOf(element)
            if (currentWidth + measureResult.size.width > fullWidth) {
                return FillResult.Filled(element)
            }
        }

        val builder = lineBuilder ?: ReaderLineBuilder(
            maxPx = fullHeight,
            initialIndent = lineIndent,
            measure = sizeOf,
        ).also {
            lineBuilder = it
        }

        when (element) {
            is AozoraElement.Ruby,
            is AozoraElement.Text,
            is AozoraElement.Heading,
            is AozoraElement.LineBreak,
            is AozoraElement.Indent,
            is AozoraElement.Illustration,
            is AozoraElement.Emphasis -> {
                when (val result = builder.tryAdd(element)) {
                    FillResult.FillContinue -> return result
                    is FillResult.Filled -> {
                        buildNewLine()
                        val remainElement = result.remainElement
                        return if (remainElement == null) {
                            // The element is consumed by new line. return continue
                            FillResult.FillContinue
                        } else {
                            tryAddElement(remainElement, lineIndent, sizeOf)
                        }
                    }
                }
            }

            AozoraElement.PageBreak -> {
                error("Never")
            }
        }
    }

    fun build(): AozoraPage {
        if (lineBuilder != null) {
            buildNewLine()
        }

        return AozoraPage(
            lines = lines.toImmutableList()
        )
    }

    private fun buildNewLine() {
        val line = lineBuilder!!.build()
        lines += line
        currentWidth += line.lineHeight
        lineBuilder = null
        Napier.d(tag = TAG) { "buildNewLine E. newLine $line, lines ${lines.size}, currentWidth $currentWidth" }
    }
}

class ReaderLineBuilder(
    private val maxPx: Float,
    initialIndent: Int = 0,
    private val measure: (AozoraElement) -> MeasureResult,
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

    fun build(): ReaderLine {
        return ReaderLine(
            lineHeight = maxWidth,
            elements = elementList.toImmutableList(),
            fontStyle = currentFontStyle
        )
    }

    private fun updateState(element: AozoraElement, measureResult: MeasureResult) {
        elementList += element
        currentHeight += measureResult.size.height
        maxWidth = maxOf(maxWidth, measureResult.size.width)
        if (measureResult.fontStyle != null) {
            currentFontStyle = measureResult.fontStyle
        }
    }
}