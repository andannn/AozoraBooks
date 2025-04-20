package me.andannn.aosora.core.pager

import androidx.compose.ui.geometry.Size
import me.andannn.aosora.core.parser.AozoraBlock
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.internal.util.divide
import me.andannn.aosora.core.parser.size
import org.jetbrains.annotations.VisibleForTesting


sealed interface FillResult {
    data class Filled(
        val remainElement: AozoraElement? = null,
        val remainBlock: AozoraBlock? = null,
    ) : FillResult

    data object FillContinue : FillResult
}

class ReaderPageBuilder(
    meta: PageMetaData,
) {
    private val fullWidth = meta.renderWidth
    private val fullHeight = meta.renderHeight
    private val lines = mutableListOf<ReaderLine>()

    private var currentWidth: Int = 0
    private var lineBuilder: ReaderLineBuilder? = null

    private var isPageBreakAdded = false

    fun tryAddBlock(block: AozoraBlock): FillResult {
        val remainingElements = block.elements.toMutableList()

        while (remainingElements.isNotEmpty()) {
            val element = remainingElements.first()
            val result = tryAddElement(
                element,
                lineIndent = block.style.blockIndent,
                sizeOf = { it.size(block.style) }
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
        sizeOf: (AozoraElement) -> Size
    ): FillResult {
        if (isPageBreakAdded) {
            return FillResult.Filled(element)
        }

        if (element is AozoraElement.PageBreak) {
            isPageBreakAdded = true
            return FillResult.FillContinue
        }

        if (lineBuilder == null) {
            val size = sizeOf(element)
            if (currentWidth + size.width > fullWidth) {
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
            meta = PageMetaData(renderHeight = fullHeight, renderWidth = fullWidth),
            lines = lines
        )
    }

    private fun buildNewLine() {
        val line = lineBuilder!!.build()
        lines += line
        currentWidth += line.lineHeight
        lineBuilder = null
    }
}

class ReaderLineBuilder(
    private val maxPx: Int,
    initialIndent: Int = 0,
    private val measure: (AozoraElement) -> Size,
) {
    private var currentHeight: Float = 0f
    private var maxWidth: Float = 0f
    private val elementList = mutableListOf<AozoraElement>()

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

            is AozoraElement.Illustration,
            is AozoraElement.Indent -> {
                if (elementList.isNotEmpty()) {
                    error("indent, and image can only be add to new line")
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
            lineHeight = maxWidth.toInt(),
            elements = elementList.toList()
        )
    }
}