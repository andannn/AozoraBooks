package me.andannn.aosora.core.common.model

/**
 * // TODO : add comment
 */
data class AozoraBlock(
    val elements: List<AozoraElement>,
    val blockType: BlockType,
    val byteRange: LongRange
) {
    val textCount: Int by lazy {
        elements.fold(0) { acc, element ->
            acc + element.length
        }
    }

    val fullText: String by lazy {
        elements.fold("") { acc, element ->
            acc + element.debugText()
        }
    }
}
