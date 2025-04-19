package me.andannn.aosora.common.parser

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

/**
 * 表示一个段落的排版样式，用于控制字体大小、行高、段距、缩进与对齐方式。
 */
data class BlockStyle(
    /**
     * 字体大小，单位为像素。用于计算字符高度和推荐的行高。
     */
    val fontSize: Int,

    /**
     * 每一行的实际高度（像素）。
     * 常见设置：行高 = fontSize × 行距倍数（如 1.5）
     *
     * 例如：
     * fontSize = 16
     * lineHeight = 24  (即 16 × 1.5)
     */
    val lineHeight: Int,

    /**
     * 段落整体的缩进字符数，作用于所有行。
     * 通常用于中见出し、引用块、注解等。
     *
     * 实际像素偏移量 = blockIndent × 单个字符宽度
     */
    val blockIndent: Int = 0,
)
