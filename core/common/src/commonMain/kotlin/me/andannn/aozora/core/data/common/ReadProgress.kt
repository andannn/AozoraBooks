package me.andannn.aozora.core.data.common

sealed interface ReadProgress {
    /**
     * Open book first time and not yet start reading.
     */
    data object None : ReadProgress

    /**
     * Reading book main content.
     *
     * @param blockIndex current block index.
     */
    data class Reading(val blockIndex: Int) : ReadProgress

    /**
     * Read the last page of book
     */
    data object Done: ReadProgress
}