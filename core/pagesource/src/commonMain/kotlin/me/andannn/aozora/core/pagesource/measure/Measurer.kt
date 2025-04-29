package me.andannn.aozora.core.pagesource.measure

import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.AozoraBlock
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.RenderSetting
import me.andannn.aozora.core.data.common.resolveFontStyle
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.data.common.BlockType
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.PaperLayout
import kotlin.collections.get
import kotlin.math.ceil

data class ElementMeasureResult(
    val size: Size,
    val fontStyle: FontStyle? = null,
)

data class BlockMeasureResult(
    val lineCount: Int,
    val lineHeightPerLine: Float,
    val availableRenderHeight: Float,
    val fontStyle: FontStyle? = null,
) {
    val totalLineHeight: Float
        get() = lineCount * lineHeightPerLine
}

fun interface BlockMeasurer {
    fun measure(block: AozoraBlock): BlockMeasureResult
}

fun interface ElementMeasurer {
    fun measure(element: AozoraElement, style: AozoraTextStyle?): ElementMeasureResult
}

class DefaultMeasurer(
    private val renderSetting: RenderSetting,
    private val layout: PaperLayout,
) : ElementMeasurer, BlockMeasurer {
    constructor(meta: PageMetaData) : this(meta, meta)

    private val fontStyleCache = mutableMapOf<AozoraTextStyle, FontStyle>()
    private val renderHeight = layout.renderHeight

    override fun measure(
        element: AozoraElement,
        style: AozoraTextStyle?
    ): ElementMeasureResult {
        return sizeOf(element, style)
    }

    override fun measure(block: AozoraBlock): BlockMeasureResult {
        val blockType = block.blockType
        when (blockType) {
            BlockType.Image -> return BlockMeasureResult(
                lineCount = 1,
                lineHeightPerLine = sizeOf(block.elements[0]).size.width,
                availableRenderHeight = renderHeight
            )

            is BlockType.TextType -> {
                val style =
                    fontStyleCache[blockType.style] ?: resolveAndSave(blockType.style)
                val lineHeight = style.lineHeight

                if (block.elements.size == 1 && block.elements[0] is AozoraElement.LineBreak) {
                    return BlockMeasureResult(
                        lineCount = 1,
                        lineHeightPerLine = lineHeight,
                        availableRenderHeight = renderHeight
                    )
                }


                val totalHeight = block.textCount * style.baseSize
                val indent = blockType.indent
                val indentHeight = style.baseSize * indent
                val availableRenderHeight = renderHeight - indentHeight
                return BlockMeasureResult(
                    lineCount = ceil(totalHeight / (availableRenderHeight)).toInt(),
                    lineHeightPerLine = lineHeight,
                    fontStyle = style,
                    availableRenderHeight = availableRenderHeight
                )
            }
        }
    }

    private fun sizeOf(
        element: AozoraElement,
        aozoraStyle: AozoraTextStyle? = null
    ): ElementMeasureResult {
        val cachedStyle = fontStyleCache[aozoraStyle]
        when (element) {
            is AozoraElement.BaseText -> {
                val style = cachedStyle ?: resolveAndSave(aozoraStyle!!)
                return ElementMeasureResult(
                    size = Size(
                        style.lineHeight,
                        style.baseSize * element.length
                    ),
                    fontStyle = style
                )
            }

            is AozoraElement.Illustration -> {
                return ElementMeasureResult(
                    size = Size(
                        element.width?.toFloat() ?: 0f,
                        element.height?.toFloat() ?: 0f
                    )
                )
            }

            AozoraElement.LineBreak -> {
                // TODO: Line break element can be measured ?
                val style = cachedStyle ?: resolveAndSave(AozoraTextStyle.PARAGRAPH)
                return ElementMeasureResult(
                    fontStyle = style,
                    size = Size(
                        style.baseSize.toFloat() * style.lineHeightMultiplier,
                        0f
                    )
                )
            }

            is AozoraElement.Indent -> {
                val style = cachedStyle ?: resolveAndSave(aozoraStyle!!)
                return ElementMeasureResult(
                    Size(
                        style.lineHeight,
                        style.baseSize.toFloat() * element.count
                    )
                )
            }

            AozoraElement.PageBreak -> error("error")
        }
    }

    private fun resolveAndSave(aozoraStyle: AozoraTextStyle): FontStyle {
        return aozoraStyle.resolveFontStyle(
            fontSizeLevel = renderSetting.fontSizeLevel,
            lineSpacing = renderSetting.lineSpacing,
            fontType = renderSetting.fontType,
        ).also {
            fontStyleCache[aozoraStyle] = it
            return it
        }
    }
}