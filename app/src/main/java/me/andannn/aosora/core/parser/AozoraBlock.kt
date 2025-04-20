package me.andannn.aosora.core.parser

import androidx.compose.ui.geometry.Size

/**
 * block can be a main context line, heading or image.
 *
 * or like this special block:
 * ［＃ここから２字下げ］
 * ［＃ここから３８字詰め］
 * 　巻頭の...
 * ［＃ここで字詰め終わり］
 * ［＃ここで字下げ終わり］
 */
data class AozoraBlock(
    val elements: List<AozoraElement>,
    val style: BlockStyle,
    val blockType: BlockType,
)

fun AozoraElement.size(style: BlockStyle): Size {
    when (this) {
        is AozoraElement.BaseText -> {
            return Size(
                style.fontSize.toFloat() * style.lineHeightMultiplier,
                style.fontSize.toFloat() * length
            )
        }
        is AozoraElement.Illustration -> {
            return Size(
                width?.toFloat() ?: 0f,
                height?.toFloat() ?: 0f
            )
        }
        AozoraElement.LineBreak -> {
            return Size(
                style.fontSize.toFloat() * style.lineHeightMultiplier,
                0f
            )
        }
        is AozoraElement.Indent -> {
            return Size(
                style.fontSize.toFloat() * style.lineHeightMultiplier,
                style.fontSize.toFloat() * count
            )
        }
        AozoraElement.PageBreak -> error("error")
    }
}

data class BlockStyle(
    /**
     * 字体大小，单位为像素。用于计算字符高度和推荐的行高。
     */
    val fontSize: Int,

    /**
     * 每一行的行距倍数。
     */
    val lineHeightMultiplier: Float,

    /**
     * 段落整体的缩进字符数，作用于所有行。
     * 通常用于中见出し、引用块、注解等。
     *
     * 实际像素偏移量 = blockIndent × 单个字符宽度
     */
    val blockIndent: Int = 0,
)
