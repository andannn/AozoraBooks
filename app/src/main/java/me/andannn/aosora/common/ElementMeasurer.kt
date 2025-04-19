package me.andannn.aosora.common

import androidx.compose.ui.geometry.Size
import me.andannn.aosora.common.parser.AozoraElement

fun interface ElementMeasurer {
    fun measure(element: AozoraElement): Size
}

class DefaultElementMeasurer(
    private val charWidth: Float = 20f,
    private val lineHeight: Float = 40f,
    private val rubyExtraHeight: Float = 20f
) : ElementMeasurer {
    override fun measure(element: AozoraElement): Size {
        val width = when (element) {
            is AozoraElement.Text,
            is AozoraElement.Emphasis,
            is AozoraElement.Heading -> element.text.length * charWidth

            is AozoraElement.Ruby -> element.text.length * charWidth
            is AozoraElement.Indent -> 2 * charWidth
            AozoraElement.LineBreak -> 0f
            AozoraElement.PageBreak -> 0f
            is AozoraElement.Illustration -> element.width?.toFloat() ?: 0f
        }

        val height = when (element) {
            is AozoraElement.Heading -> lineHeight + 10
            is AozoraElement.Ruby -> lineHeight + rubyExtraHeight
            is AozoraElement.Text,
            is AozoraElement.Emphasis -> lineHeight
            is AozoraElement.Indent -> lineHeight
            AozoraElement.LineBreak -> lineHeight
            AozoraElement.PageBreak -> 0f
            is AozoraElement.Illustration -> element.height?.toFloat() ?: lineHeight
        }

        return Size(width, height)
    }
}
