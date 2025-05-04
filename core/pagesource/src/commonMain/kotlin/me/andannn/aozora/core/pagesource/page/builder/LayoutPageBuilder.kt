package me.andannn.aozora.core.pagesource.page.builder

import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraLayoutPage
import me.andannn.aozora.core.data.common.AozoraPage.AozoraRoughPage
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.data.common.Block
import me.andannn.aozora.core.data.common.Line
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.pagesource.measure.DefaultMeasurer
import me.andannn.aozora.core.pagesource.measure.ElementMeasureResult
import me.andannn.aozora.core.pagesource.measure.ElementMeasurer

private const val TAG = "ReaderPageBuilder"

fun AozoraPage.layout(): AozoraLayoutPage {
    return when (this) {
        is AozoraLayoutPage -> return this
        is AozoraRoughPage -> {
            val builder =
                LayoutPageBuilder(pageMetaData, DefaultMeasurer(pageMetaData), forceAddBlock = true)
            this.blocks.forEach {
                builder.tryAddBlock(it)
            }
            return builder.build()
        }

        is AozoraPage.AozoraCoverPage ->
            createCoverPage(
                pageMetaData,
                title = title,
                author = author,
                subtitle = subtitle,
            ).layout()
    }
}

private fun createCoverPage(
    pageMetaData: PageMetaData,
    title: String,
    subtitle: String?,
    author: String,
): AozoraRoughPage =
    AozoraRoughPage(
        pageMetaData = pageMetaData,
        blocks =
            listOfNotNull<Block>(
                Block.Heading(
                    blockIndex = -1,
                    textStyle = AozoraTextStyle.HEADING_LARGE,
                    indent = 2,
                    byteRange = 0L..0L,
                    elements =
                        listOfNotNull(
                            AozoraElement.Text(
                                text = title,
                            ),
                            AozoraElement.LineBreak,
                        ),
                ),
                subtitle?.let {
                    Block.Heading(
                        blockIndex = -1,
                        textStyle = AozoraTextStyle.HEADING_LARGE,
                        indent = 2,
                        byteRange = 0L..0L,
                        elements =
                            listOfNotNull(
                                AozoraElement.Text(
                                    text = it,
                                ),
                                AozoraElement.LineBreak,
                            ),
                    )
                },
                Block.Heading(
                    blockIndex = -1,
                    textStyle = AozoraTextStyle.HEADING_MEDIUM,
                    indent = 3,
                    byteRange = 0L..0L,
                    elements =
                        listOfNotNull(
                            AozoraElement.Text(
                                text = author,
                            ),
                            AozoraElement.LineBreak,
                        ),
                ),
            ).toImmutableList(),
    )

class LayoutPageBuilder(
    private val meta: PageMetaData,
    private val measurer: ElementMeasurer,
    private val forceAddBlock: Boolean = false,
) : PageBuilder<AozoraLayoutPage> {
    private val fullWidth: Float = meta.renderWidth
    private val fullHeight: Float = meta.renderHeight

    private val lines = mutableListOf<Line>()

    private var currentWidth: Float = 0f
    private var lineBuilder: LineBuilder? = null

    private var isPageBreakAdded = false

    override fun tryAddBlock(block: Block): FillResult {
        Napier.v(tag = TAG) { "tryAddBlock E. block $block" }
        val remainingElements = block.elements.toMutableList()

        while (remainingElements.isNotEmpty()) {
            val element = remainingElements.first()
            val result =
                tryAddElement(
                    element,
                    lineIndent = (block as? Block.TextBlock)?.indent ?: 0,
                    sizeOf = { element ->
                        measurer.measure(
                            element,
                            (block as? Block.TextBlock)?.textStyle,
                        )
                    },
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
            FillResult.Filled(remainBlock = block.copyWith(remainingElements))
        }
    }

    fun tryAddElement(
        element: AozoraElement,
        lineIndent: Int,
        sizeOf: (AozoraElement) -> ElementMeasureResult,
    ): FillResult {
        Napier.v(tag = TAG) { "tryAddElement E. element $element" }
        if (isPageBreakAdded) {
            return FillResult.Filled(element)
        }

        if (element is AozoraElement.PageBreak) {
            isPageBreakAdded = true
            return FillResult.FillContinue
        }

        if (!forceAddBlock && lineBuilder == null) {
            val measureResult = sizeOf(element)
            if (currentWidth + measureResult.size.width > fullWidth) {
                return FillResult.Filled(element)
            }
        }

        val builder =
            lineBuilder ?: LineBuilder(
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
            is AozoraElement.Emphasis,
            -> {
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

    override fun build(): AozoraLayoutPage {
        if (lineBuilder != null) {
            buildNewLine()
        }

        return AozoraLayoutPage(
            pageMetaData = meta,
            lines = lines.toImmutableList(),
        )
    }

    private fun buildNewLine() {
        val line = lineBuilder!!.build()
        lines += line
        currentWidth += line.lineHeight
        lineBuilder = null
        Napier.v(tag = TAG) { "buildNewLine E. newLine $line, lines ${lines.size}, currentWidth $currentWidth" }
    }
}
