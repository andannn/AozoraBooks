/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.IntSize
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.ui.common.theme.RandomColor
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.DEBUG_RENDER
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.ElementRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.MeasureHelper

abstract class BasicTextRenderAdapterV2<T : AozoraElement.BaseText>(
    private val measureHelper: MeasureHelper,
) : ElementRenderAdapterV2<T> {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: T,
        fontStyle: FontStyle?,
    ): Size? = drawWithScope(scope = this, x, y, element, fontStyle)

    protected fun drawWithScope(
        scope: DrawScope,
        x: Float,
        y: Float,
        element: T,
        fontStyle: FontStyle?,
    ): Size {
        if (fontStyle == null) {
            error("fontStyle must not be null $element")
        }

        val drawSize =
            scope.drawVerticalString(
                text = element.text,
                fontStyle = fontStyle,
                x = x,
                y = y,
            )

        if (DEBUG_RENDER) {
            scope.drawRect(
                color = RandomColor,
                topLeft = Offset(x - drawSize.width / 2, y),
                size = drawSize,
            )
        }

        return drawSize
    }

    protected fun DrawScope.drawVerticalString(
        text: String,
        fontStyle: FontStyle,
        x: Float,
        y: Float,
        isNotation: Boolean = false,
    ): Size {
        var currentY = y
        var maxWidth = 0f

        text.consume { type, string ->
            val charSize =
                when (type) {
                    ConsumeType.Japanese -> {
                        drawKanji(
                            char = string,
                            x = x,
                            currentY = currentY,
                            fontStyle = fontStyle,
                            isNotation = isNotation,
                        )
                    }

                    ConsumeType.AlphaNumeric -> {
                        drawAlphaNumeric(
                            string = string,
                            x = x,
                            currentY = currentY,
                            fontStyle = fontStyle,
                        )
                    }
                }

            currentY += charSize.height
            maxWidth = maxOf(maxWidth, charSize.width.toFloat())
        }

        val width = maxWidth
        val height = currentY - y
        return Size(width, height)
    }

    private fun DrawScope.drawAlphaNumeric(
        string: String,
        x: Float,
        currentY: Float,
        fontStyle: FontStyle,
        isNotation: Boolean = false,
    ): IntSize {
        val result =
            measureHelper.measure(
                text = string,
                fontStyle = fontStyle,
                isNotation = isNotation,
            )
        val size = result.size
        rotate(
            degrees = 90f,
            pivot = Offset(x, currentY),
        ) {
            drawText(
                textLayoutResult = result,
                topLeft =
                    Offset(
                        x = x,
                        y = currentY - size.height.div(2f),
                    ),
            )
        }
        return IntSize(size.height, size.width)
    }

    private fun DrawScope.drawKanji(
        char: String,
        x: Float,
        currentY: Float,
        fontStyle: FontStyle,
        isNotation: Boolean = false,
    ): IntSize {
        val result =
            measureHelper.measure(
                text = char,
                fontStyle = fontStyle,
                isNotation = isNotation,
            )
        val charSize = result.size
        val offsetY = (charSize.height - charSize.width) / 2
        val offsetX = (charSize.width) / 2
        withCharTransforms(
            char,
            charSize.width,
            onGetPivot = {
                Offset(
                    x = x,
                    y = currentY + (charSize.width) / 2,
                )
            },
        ) {
            drawText(
                textLayoutResult = result,
                topLeft =
                    Offset(
                        x = x - offsetX,
                        y = currentY - offsetY,
                    ),
            )
        }
        return IntSize(charSize.height, charSize.width)
    }
}

private enum class ConsumeType {
    Japanese,
    AlphaNumeric,
}

private inline fun String.consume(consumer: (consumeType: ConsumeType, value: String) -> Unit) {
    if (isEmpty()) return

    var i = 0
    val length = this.length

    while (i < length) {
        val c = this[i]

        when {
            c.isAsciiAlphaNumeric() -> {
                val start = i
                i++
                while (i < length && this[i].isAsciiAlphaNumeric()) {
                    i++
                }
                consumer(ConsumeType.AlphaNumeric, substring(start, i))
            }

            else -> {
                consumer(ConsumeType.Japanese, c.toString())
                i++
            }
        }
    }
}

private fun Char.isAsciiAlphaNumeric(): Boolean = (this in 'A'..'Z') || (this in 'a'..'z') || (this in '0'..'9')

private sealed interface TransForm {
    data class Rotate(
        val rotate: Float,
    ) : TransForm

    data class Translate(
        val dx: Float = 0f,
        val dy: Float = 0f,
    ) : TransForm

    data class TranslateFactor(
        val factorX: Float = 0f,
        val factorY: Float = 0f,
    ) : TransForm
}

private val TransformMap =
    mapOf(
        '、' to listOf(TransForm.TranslateFactor(0.7f, -0.6f)),
        '。' to listOf(TransForm.Rotate(180f), TransForm.Translate(0f, -10f)),
        '「' to listOf(TransForm.Rotate(90f)),
        '」' to listOf(TransForm.Rotate(90f)),
        '（' to listOf(TransForm.Rotate(90f)),
        '）' to listOf(TransForm.Rotate(90f)),
        'ー' to listOf(TransForm.Rotate(90f)),
        '…' to listOf(TransForm.Rotate(90f)),
        '〜' to listOf(TransForm.Rotate(90f)),
        '―' to listOf(TransForm.Rotate(90f)),
        '『' to listOf(TransForm.Rotate(90f)),
        '』' to listOf(TransForm.Rotate(90f)),
        '【' to listOf(TransForm.Rotate(90f)),
        '】' to listOf(TransForm.Rotate(90f)),
        '［' to listOf(TransForm.Rotate(90f)),
        '］' to listOf(TransForm.Rotate(90f)),
        '～' to listOf(TransForm.Rotate(90f)),
    )

private fun DrawScope.withCharTransforms(
    char: String,
    oneCharSize: Int,
    onGetPivot: () -> Offset,
    block: DrawScope.() -> Unit,
) {
    val transforms = mutableListOf<TransForm>()
    TransformMap[char.first()]?.let {
        transforms.addAll(it)
    }
    if (transforms.isEmpty()) {
        block()
    } else {
        applyTransformsRecursively(transforms, 0, oneCharSize, onGetPivot, block)
    }
}

private fun DrawScope.applyTransformsRecursively(
    transforms: List<TransForm>,
    index: Int,
    charSize: Int,
    onGetPivot: () -> Offset,
    block: DrawScope.() -> Unit,
) {
    if (index >= transforms.size) {
        block()
        return
    }

    val current = transforms[index]
    val next: DrawScope.() -> Unit = {
        applyTransformsRecursively(transforms, index + 1, charSize, onGetPivot, block)
    }

    when (current) {
        is TransForm.Rotate -> {
            rotate(
                degrees = current.rotate,
                pivot = onGetPivot(),
                block = next,
            )
        }

        is TransForm.Translate -> {
            translate(
                left = current.dx,
                top = current.dy,
                block = next,
            )
        }

        is TransForm.TranslateFactor -> {
            val dx = charSize * current.factorX
            val dy = charSize * current.factorY
            translate(
                left = dx,
                top = dy,
                block = next,
            )
        }
    }
}
