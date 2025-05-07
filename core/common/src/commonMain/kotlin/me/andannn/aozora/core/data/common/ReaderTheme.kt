package me.andannn.aozora.core.data.common

/**
 * The theme of the reader.
 */
enum class ReaderTheme {
    /**
     * Black and white theme.
     */
    MONOCHROME,

    /**
     * Follow system theme.
     */
    DYNAMIC,

    /**
     * Sepia-toned theme that mimics paper.
     */
    PAPER,

    GREEN_EYE_CARE,

    ;

    companion object {
        val DEFAULT = DYNAMIC
    }
}
