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
import kotlin.text.forEach

abstract class BasicTextRenderAdapterV2(
    private val measureHelper: MeasureHelper,
) : ElementRenderAdapterV2 {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
    ): Size? = drawWithScope(scope = this, x, y, element, fontStyle)

    protected fun drawWithScope(
        scope: DrawScope,
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
    ): Size? {
        if (element !is AozoraElement.BaseText) return null
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

        text.forEach { char ->
            val charSize =
                drawKanji(
                    char = char,
                    x = x,
                    currentY = currentY,
                    fontStyle = fontStyle,
                    isNotation = isNotation,
                )

            currentY += charSize.width
            maxWidth = maxOf(maxWidth, charSize.width.toFloat())
        }

        val width = maxWidth
        val height = currentY - y
        return Size(width, height)
    }

    private fun DrawScope.drawKanji(
        char: Char,
        x: Float,
        currentY: Float,
        fontStyle: FontStyle,
        isNotation: Boolean = false,
    ): IntSize {
        val result =
            measureHelper.measure(
                text = char.toString(),
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
        return result.size
    }
}

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
    )

private fun isUtf8AlphaBet(char: Char): Boolean = char in 'A'..'Z' || char in 'a'..'z'

private fun DrawScope.withCharTransforms(
    char: Char,
    oneCharSize: Int,
    onGetPivot: () -> Offset,
    block: DrawScope.() -> Unit,
) {
    val transforms = mutableListOf<TransForm>()
    TransformMap[char]?.let {
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
