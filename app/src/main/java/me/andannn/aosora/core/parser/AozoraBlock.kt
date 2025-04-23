package me.andannn.aosora.core.parser

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
    val blockType: BlockType,
    val blockIndex: Int = -1
)
