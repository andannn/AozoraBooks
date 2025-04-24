package me.andannn.aosora.core.common.model

/**
 * // TODO : add comment
 */
data class AozoraBlock(
    val elements: List<AozoraElement>,
    val blockType: BlockType,
    val blockIndex: Int = -1
)
