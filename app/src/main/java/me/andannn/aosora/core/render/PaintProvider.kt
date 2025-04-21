package me.andannn.aosora.core.render

import android.graphics.Paint
import android.graphics.Paint.VERTICAL_TEXT_FLAG
import me.andannn.aosora.core.common.FontStyle
import kotlin.random.Random

interface PaintProvider {
    fun getPaint(fontStyle: FontStyle, isNotation: Boolean = false): Paint

    fun getDebugPaint(): Paint
}

class DefaultPaintProvider() : PaintProvider {
    private val paintCache = mutableMapOf<FontMeta, Paint>()

    override fun getPaint(
        fontStyle: FontStyle,
        isNotation: Boolean
    ): Paint {
        val fontSize = if (isNotation) fontStyle.notationSize else fontStyle.baseSize
        val fontMeta = FontMeta(fontSize)

        return paintCache[fontMeta]
            ?: Paint()
                .apply {
                    textSize = fontMeta.fontSize
                    flags = flags or VERTICAL_TEXT_FLAG
                }
                .also { paintCache[fontMeta] = it }
    }

    override fun getDebugPaint(): Paint {
        return Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 3f
            // random color
            setARGB(
                256 / 3,
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            )
        }
    }
}

private data class FontMeta(
    val fontSize: Float
)