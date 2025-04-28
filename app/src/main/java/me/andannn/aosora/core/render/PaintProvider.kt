package me.andannn.aosora.core.render

import android.graphics.Paint
import android.graphics.Paint.VERTICAL_TEXT_FLAG
import me.andannn.aosora.core.common.model.FontType
import me.andannn.aosora.core.common.model.FontStyle
import me.andannn.aosora.core.common.model.getTypeface
import kotlin.random.Random

interface PaintProvider {
    fun getPaint(
        fontStyle: FontStyle,
        isNotation: Boolean = false,
        textColor: Int,
    ): Paint

    fun getDebugPaint(): Paint
}

class DefaultPaintProvider() : PaintProvider {
    private val paintCache = mutableMapOf<FontMeta, Paint>()

    override fun getPaint(
        fontStyle: FontStyle,
        isNotation: Boolean,
        textColor: Int,
    ): Paint {
        val fontSize = if (isNotation) fontStyle.notationSize else fontStyle.baseSize
        val fontType = fontStyle.fontType
        val fontColor = textColor
        val fontMeta = FontMeta(fontSize, fontStyle.fontType, fontColor)

        return paintCache[fontMeta]
            ?: Paint()
                .apply {
                    textSize = fontMeta.fontSize
                    flags = flags or VERTICAL_TEXT_FLAG

                    if (fontType != FontType.DEFAULT) {
                        typeface = fontType.getTypeface()
                    }

                    color = fontMeta.fontColor
                }
                .also { paintCache[fontMeta] = it }
    }

    override fun getDebugPaint(): Paint {
        return Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 3f
            // random color
            setARGB(
                256 / 6,
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            )
        }
    }
}

private data class FontMeta(
    val fontSize: Float,
    val fontType: FontType,
    val fontColor: Int,
)