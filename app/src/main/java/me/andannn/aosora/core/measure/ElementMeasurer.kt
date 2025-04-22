package me.andannn.aosora.core.measure

import androidx.compose.ui.geometry.Size
import me.andannn.aosora.core.common.FontStyle
import me.andannn.aosora.core.common.PageMetaData
import me.andannn.aosora.core.common.resolveFontStyle
import me.andannn.aosora.core.parser.AozoraElement
import me.andannn.aosora.core.parser.AozoraTextStyle

data class MeasureResult(
    val size: Size,
    val fontStyle: FontStyle? = null,
)

fun interface ElementMeasurer {
    fun measure(element: AozoraElement, style: AozoraTextStyle?): MeasureResult
}

class DefaultElementMeasurer(
    private val meta: PageMetaData
) : ElementMeasurer {
    private val fontStyleCache = mutableMapOf<AozoraTextStyle, FontStyle>()

    override fun measure(
        element: AozoraElement,
        style: AozoraTextStyle?
    ): MeasureResult {
        return sizeOf(element, style)
    }

    private fun sizeOf(
        element: AozoraElement,
        aozoraStyle: AozoraTextStyle? = null
    ): MeasureResult {
        val cachedStyle = fontStyleCache[aozoraStyle]
        when (element) {
            is AozoraElement.BaseText -> {
                val style = cachedStyle ?: resolveAndSave(aozoraStyle!!)
                return MeasureResult(
                    size = Size(
                        style.baseSize.toFloat() * style.lineHeightMultiplier,
                        style.baseSize.toFloat() * element.length
                    ),
                    fontStyle = style
                )
            }

            is AozoraElement.Illustration -> {
                return MeasureResult(
                    size = Size(
                        element.width?.toFloat() ?: 0f,
                        element.height?.toFloat() ?: 0f
                    )
                )
            }

            AozoraElement.LineBreak -> {
                // TODO: Link break element can be measured ?
                val style = cachedStyle ?: resolveAndSave(AozoraTextStyle.PARAGRAPH)
                return MeasureResult(
                    fontStyle = style,
                    size = Size(
                        style.baseSize.toFloat() * style.lineHeightMultiplier,
                        0f
                    )
                )
            }

            is AozoraElement.Indent -> {
                val style = cachedStyle ?: resolveAndSave(aozoraStyle!!)
                return MeasureResult(
                    Size(
                        style.baseSize.toFloat() * style.lineHeightMultiplier,
                        style.baseSize.toFloat() * element.count
                    )
                )
            }

            AozoraElement.PageBreak -> error("error")
        }
    }

    private fun resolveAndSave(aozoraStyle: AozoraTextStyle): FontStyle {
        return aozoraStyle.resolveFontStyle(
            fontSizeLevel = meta.fontSizeLevel,
            lineSpacing = meta.lineSpacing,
            fontType = meta.fontType,
        ).also {
            fontStyleCache[aozoraStyle] = it
            return it
        }
    }
}